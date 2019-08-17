package cc.hyperium.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.gui.keybinds.GuiKeybinds;
import cc.hyperium.installer.utils.http.HttpEntity;
import cc.hyperium.installer.utils.http.HttpResponse;
import cc.hyperium.installer.utils.http.impl.client.HttpClients;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.gui.ChatFormatting;
import org.apache.commons.io.IOUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.server.MinecraftServer;
import rocks.rdil.jailbreak.BackendHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class GuiHyperiumScreenIngameMenu extends GuiHyperiumScreen {
    private static JsonHolder data = new JsonHolder();
    private long lastUpdate = 0L;

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        int i = -16;

        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + i, I18n.format("menu.returnToMenu")));

        /* If Client is on server, add disconnect button */
        if (!this.mc.isIntegratedServerRunning()) {
            (this.buttonList.get(0)).displayString = I18n.format("menu.disconnect");
        }

        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + i, I18n.format("menu.returnToGame")));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + i, 98, 20, I18n.format("menu.options")));

        GuiButton guibutton;
        this.buttonList.add(guibutton = new GuiButton(7, this.width / 2 + 2, this.height / 4 + 96 + i, 98, 20, I18n.format("menu.shareToLan")));
        this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + i, 98, 20, I18n.format("gui.achievements")));
        this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + i, 98, 20, I18n.format("gui.stats")));

        guibutton.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();

        buttonList.add(new GuiButton(9, this.width / 2 - 100, height / 4 + 56, 98, 20, "Client Settings"));
        buttonList.add(new GuiButton(8, this.width / 2 + 2, height / 4 + 56, 98, 20, "Keybinds"));

        WorldClient theWorld = Minecraft.getMinecraft().theWorld;

        // Used to detect if the player is on a singleplayer world or a multiplayer world.
        MinecraftServer integratedServer = Minecraft.getMinecraft().getIntegratedServer();
        if (theWorld != null && (integratedServer == null)) {
            GuiButton oldButton = buttonList.remove(3);
            GuiButton newButton = new GuiButton(10, oldButton.xPosition, oldButton.yPosition, oldButton.getButtonWidth(), 20, I18n.format("button.ingame.serverlist"));
            buttonList.add(newButton);
        }
    }

    @Override
    public void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;

            case 1:
                boolean integratedServerRunning = this.mc.isIntegratedServerRunning();
                button.enabled = false;
                this.mc.theWorld.sendQuittingDisconnectingPacket();
                this.mc.loadWorld(null);

                if (integratedServerRunning) {
                    Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiMainMenu());
                } else {
                    Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiMultiplayer(new GuiMainMenu()));
                }
                break;

            default:
                break;

            case 4:
                mc.displayGuiScreen(null);
                this.mc.setIngameFocus();
                break;

            case 5:
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
                break;

            case 6:
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
                break;

            case 7:
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiShareToLan(this));
                break;

            case 8:
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiKeybinds());
                break;

            case 9:
                HyperiumMainGui.INSTANCE.show();
                break;

            case 10:
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiIngameMultiplayer(Minecraft.getMinecraft().currentScreen));
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.pushMatrix();

        GlStateManager.translate(0, height - 50, 0);

        if (System.currentTimeMillis() - lastUpdate > 2000L) {
            refreshData();
        }

        ScaledResolution current = ResolutionUtil.current();
        GlStateManager.translate(current.getScaledWidth() / 2, 5, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        drawCenteredString(fontRendererObj, "HyperiumJailbreak Player Count:", 0, -5, 0xFFFFFF);
        GlStateManager.translate(0F, 10F, 0F);
        GlStateManager.scale(1, 1, 1);
        GlStateManager.enableAlpha();

        float z = 4F;

        GlStateManager.translate(0.0F, 0.0F, z);

        drawCenteredString(fontRendererObj, "Now Online: " + ChatFormatting.GREEN + data.optInt("online") + ChatFormatting.RESET, 0, 0, 0xFFFFFF);

        GlStateManager.popMatrix();
    }

    private synchronized void refreshData() {
        lastUpdate = System.currentTimeMillis() * 2;

        Multithreading.runAsync(() -> {
            HttpResponse response = null;
            try {
                response = HttpClients.createDefault().execute(
                        BackendHandler.generate("https://backend.rdil.rocks/getOnline")
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(response);
            HttpEntity entity = response.getEntity();
            System.out.println(entity);

            if (entity != null) {
                try (InputStream instream = entity.getContent()) {
                    System.out.println(instream);
                    StringWriter writer = new StringWriter();
                    IOUtils.copy(instream, writer, "UTF-8");
                    data = new JsonHolder(new JsonParser().parse(writer.toString()).getAsJsonObject());
                    System.out.println(writer.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            lastUpdate = System.currentTimeMillis();
        });
    }
}
