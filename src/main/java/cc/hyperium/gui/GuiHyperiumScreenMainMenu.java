/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.config.provider.IntegrationOptionsProvider;
import cc.hyperium.gui.hyperium.HyperiumSettingsGui;
import cc.hyperium.styles.GuiStyle;
import cc.hyperium.mixinsimp.renderer.gui.IMixinGuiMultiplayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.lwjgl.input.Keyboard;

public class GuiHyperiumScreenMainMenu extends GuiHyperiumScreen {
    private static boolean FIRST_START = true;
    private final ResourceLocation exit = new ResourceLocation("textures/material/exit.png");
    private final ResourceLocation people_outline = new ResourceLocation("textures/material/people-outline.png");
    private final ResourceLocation person_outline = new ResourceLocation("textures/material/person-outline.png");
    private final ResourceLocation settings = new ResourceLocation("textures/material/settings.png");
    private final ResourceLocation hIcon = new ResourceLocation("textures/h_icon.png");
    private GuiScreen parentScreen;
    private boolean field_183502_L;

    public GuiHyperiumScreenMainMenu() {
        if (Minecraft.getMinecraft().isFullScreen() && IntegrationOptionsProvider.WINDOWED_FULLSCREEN && FIRST_START) {
            GuiHyperiumScreenMainMenu.FIRST_START = false;
            Minecraft.getMinecraft().toggleFullscreen();
            Minecraft.getMinecraft().toggleFullscreen();
        }
    }

    public void initGui() {
        viewportTexture = new DynamicTexture(256, 256);
        int j = this.height / 4 + 48;

        this.addSingleplayerMultiplayerButtons(j - 10);

        if(getStyle() == GuiStyle.DEFAULT) {
            addDefaultStyleOptionsButton(j);
        } else {
            addHyperiumStyleOptionsButton();
        }

        this.mc.setConnectedToRealms(false);

        if (Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS) && !this.field_183502_L) {
            RealmsBridge realmsbridge = new RealmsBridge();
            this.parentScreen = realmsbridge.getNotificationScreen(this);
            this.field_183502_L = true;
        }

        if (Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS) && this.parentScreen != null) {
            this.parentScreen.func_183500_a(this.width, this.height);
        }
    }

    private void addSingleplayerMultiplayerButtons(int p_73969_1_) {
        if(getStyle() == GuiStyle.DEFAULT) {
            addDefaultStyleSingleplayerMultiplayerButtons(p_73969_1_);
        } else {
            addHyperiumStyleSingleplayerMultiplayerButtons();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiOptions(this, this.mc.gameSettings));
        }

        if (button.id == 5) {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }

        if (button.id == 1) {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiSelectWorld(this));
        }

        if (button.id == 2) {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiMultiplayer(this));
        }

        if (button.id == 4) {
            this.mc.shutdown();
        }

        if (button.id == 11) {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
        }

        if (button.id == 12) {
            ISaveFormat isaveformat = this.mc.getSaveLoader();
            WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

            if (worldinfo != null) {
                GuiYesNo guiyesno = GuiSelectWorld.func_152129_a(this, worldinfo.getWorldName(), 12);
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(guiyesno);
            }
        }

        if (getStyle() == GuiStyle.DEFAULT) {
            if (button.id == 15) HyperiumSettingsGui.INSTANCE.show();
            if (button.id == 16) {
                GuiMultiplayer p_i1182_1_ = new GuiMultiplayer(new GuiMainMenu());
                p_i1182_1_.setWorldAndResolution(Minecraft.getMinecraft(), width, height);
                ((IMixinGuiMultiplayer) p_i1182_1_).makeDirectConnect();
                String hostName = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? "stuck.hypixel.net" : "mc.hypixel.net";
                ServerData data = new ServerData("hypixel", hostName, false);
                ((IMixinGuiMultiplayer) p_i1182_1_).setIp(data);
                p_i1182_1_.confirmClicked(true, 0);
             }
        } else {
            if (button.id == 15) HyperiumSettingsGui.INSTANCE.show();
        }

        if (button.id == 100) {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(HyperiumSettingsGui.INSTANCE);
        }
    }

    private void addHyperiumStyleSingleplayerMultiplayerButtons() {
        this.buttonList.add(new GuiButton(1, this.width / 2 - getIntendedWidth(295), this.height / 2 - getIntendedHeight(55), getIntendedWidth(110), getIntendedHeight(110), ""));
        this.buttonList.add(new GuiButton(2, this.width / 2 - getIntendedWidth(175), this.height / 2 - getIntendedHeight(55), getIntendedWidth(110), getIntendedHeight(110), ""));
        this.buttonList.add(new GuiButton(15, this.width / 2 + getIntendedWidth(65), this.height / 2 - getIntendedHeight(55), getIntendedWidth(110), getIntendedHeight(110), ""));
    }

    private void addDefaultStyleSingleplayerMultiplayerButtons(int p_73969_1_) {
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer")));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_73969_1_ + 24, I18n.format("menu.multiplayer")));
        // Change realms button ID to 16 to avoid conflicts
        this.buttonList.add(this.hypixelButton = new GuiButton(16, this.width / 2 - 100, p_73969_1_ + 48, 200, 20, "Join Hypixel"));

        this.buttonList.add(new GuiButton(15, this.width / 2 - 100, p_73969_1_ + 72, "Hyperium Settings"));
    }

    private void addHyperiumStyleOptionsButton() {
        this.buttonList.add(new GuiButton(0, width / 2 - getIntendedWidth(55), height / 2 - getIntendedHeight(55), getIntendedWidth(110), getIntendedHeight(110), ""));
        this.buttonList.add(new GuiButton(4, width / 2 + getIntendedWidth(185), height / 2 - getIntendedHeight(55), getIntendedWidth(110), getIntendedHeight(110), ""));
    }

    private void addDefaultStyleOptionsButton(int j) {
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, j + 56 + 12 + 24 - 5, 98, 20, I18n.format("menu.options")));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 2, j + 56 + 12 + 24 - 5, 98, 20, I18n.format("menu.quit")));
    }

    @Override
    public void drawHyperiumStyleScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();

        super.drawHyperiumStyleScreen(mouseX, mouseY, partialTicks);
        // Draw icons on buttons
        TextureManager tm = mc.getTextureManager();

        tm.bindTexture(person_outline);
        drawScaledCustomSizeModalRect(this.width / 2 - getIntendedWidth(285), this.height / 2 - getIntendedHeight(45), 0, 0, 192, 192, getIntendedWidth(90), getIntendedHeight(90), 192, 192);
        tm.bindTexture(people_outline);
        drawScaledCustomSizeModalRect(this.width / 2 - getIntendedWidth(165), this.height / 2 - getIntendedHeight(45), 0, 0, 192, 192, getIntendedWidth(90), getIntendedHeight(90), 192, 192);
        tm.bindTexture(settings);
        drawScaledCustomSizeModalRect(this.width / 2 - getIntendedWidth(45), this.height / 2 - getIntendedHeight(45), 0, 0, 192, 192, getIntendedWidth(90), getIntendedHeight(90), 192, 192);
        tm.bindTexture(hIcon);
        drawScaledCustomSizeModalRect(this.width / 2 + getIntendedWidth(85), this.height / 2 - getIntendedHeight(35), 0, 0, 104, 104, getIntendedWidth(70), getIntendedHeight(70), 104, 104);
        tm.bindTexture(exit);
        drawScaledCustomSizeModalRect(this.width / 2 + getIntendedWidth(195), this.height / 2 - getIntendedHeight(45), 0, 0, 192, 192, getIntendedWidth(90), getIntendedHeight(90), 192, 192);

        GlStateManager.popMatrix();
    }
}
