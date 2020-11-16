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

package cc.hyperium.mods.levelhead.guis;

import cc.hyperium.Hyperium;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mods.levelhead.Levelhead;
import cc.hyperium.mods.levelhead.config.LevelheadConfig;
import cc.hyperium.mods.levelhead.renderer.LevelheadComponent;
import cc.hyperium.mods.levelhead.renderer.LevelheadTag;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.client.config.GuiSlider;
import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.input.Keyboard;
import java.awt.Color;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class LevelHeadGui extends GuiScreen {
    private static final String COLOR_CHAR = "\u00a7";
    private static final String colors = "0123456789abcdef";
    private final List<GuiButton> sliders = new ArrayList<>();
    private final Map<GuiButton, Consumer<GuiButton>> clicks = new HashMap<>();
    private final Minecraft mc;
    private final ReentrantLock lock = new ReentrantLock();
    private final Levelhead mod;
    private GuiButton headerColorButton;
    private GuiButton footerColorButton;
    private GuiButton prefixButton;
    private boolean isCustom = false;
    private GuiTextField textField;

    private int calculateHeight(int row) {
        return 55 + row * 23;
    }

    public LevelHeadGui(Levelhead modIn) {
        this.mod = modIn;
        this.mc = Minecraft.getMinecraft();
    }

    private void reg(GuiButton button, Consumer<GuiButton> consumer) {
        this.buttonList.add(button);
        this.clicks.put(button, consumer);
    }

    @Override
    public void initGui() {
        Multithreading.runAsync(() -> {
            String raw = Sk1erMod.getInstance().rawWithAgent("https://api.sk1er.club/levelhead/" + Minecraft.getMinecraft().getSession().getProfile().getId().toString().replace("-", ""));
            this.isCustom = new JsonHolder(raw).optBoolean("custom");
            lock.lock();
            lock.unlock();
        });
        Keyboard.enableRepeatEvents(true);

        Levelhead instance = Hyperium.INSTANCE.getModIntegration().getLevelhead();
        reg(new GuiButton(2, this.width / 2 - 155, calculateHeight(4), 150, 20, "Header Mode: " + getMode(true)), button -> {
            if (LevelheadConfig.headerRgb) {
                LevelheadConfig.headerRgb = false;
                LevelheadConfig.headerChroma = true;
            } else if (LevelheadConfig.headerChroma) {
                LevelheadConfig.headerRgb = true;
                LevelheadConfig.headerChroma = false;
            } else {
                LevelheadConfig.headerRgb = true;
                LevelheadConfig.headerChroma = false;
            }
            button.displayString = "Header Mode: " + getMode(true);
        });

        reg(new GuiButton(3, this.width / 2 + 5, calculateHeight(4), 150, 20, "Footer Mode: " + getMode(false)), button -> {
            if (LevelheadConfig.footerRgb) {
                LevelheadConfig.footerRgb = false;
                LevelheadConfig.footerChroma = true;
            } else if (LevelheadConfig.footerChroma) {
                LevelheadConfig.footerRgb = false;
                LevelheadConfig.footerChroma = false;
            } else {
                LevelheadConfig.footerRgb = true;
                LevelheadConfig.footerChroma = false;
            }
            button.displayString = "Header Mode: " + getMode(false);
        });

        reg(this.prefixButton = new GuiButton(6, this.width / 2 + 5, calculateHeight(1), 150, 20, "Set Prefix"), button -> changePrefix());

        this.textField = new GuiTextField(0, mc.fontRendererObj, this.width / 2 - 154, calculateHeight(1), 148, 20);

        reg(this.headerColorButton = new GuiButton(4, this.width / 2 - 155, calculateHeight(5), 150, 20, "Rotate Color"), button -> {
            int primaryId = colors.indexOf(removeColorChar(LevelheadConfig.headerColor));
            if (++primaryId == colors.length()) {
                primaryId = 0;
            }
            LevelheadConfig.headerColor = COLOR_CHAR + colors.charAt(primaryId);
        });
        reg(this.footerColorButton = new GuiButton(5, this.width / 2 + 5, calculateHeight(5), 150, 20, "Rotate Color"), button -> {
            int primaryId = colors.indexOf(removeColorChar(LevelheadConfig.footerColor));
            if (++primaryId == colors.length()) {
                primaryId = 0;
            }
            LevelheadConfig.footerColor = COLOR_CHAR + colors.charAt(primaryId);
        });

        JsonHolder types = instance.getTypes();
        reg(new GuiButton(4, this.width / 2 - 155, calculateHeight(3), 150 * 2 + 10, 20, "Current Type: " + types.optJSONObject(instance.getType()).optString("name")), button -> {
            String currentType = instance.getType();
            List<String> keys = types.getKeys();
            int i = keys.indexOf(currentType);
            i++;
            if (i >= keys.size()) {
                i = 0;
            }
            if (LevelheadConfig.customHeader.equalsIgnoreCase(types.optJSONObject(currentType).optString("name"))) {
                LevelheadConfig.customHeader = types.optJSONObject(keys.get(i)).optString("name");
            }
            instance.setType(keys.get(i));
            button.displayString = "Current Type: " + types.optJSONObject(instance.getType()).optString("name");
            Hyperium.INSTANCE.getModIntegration().getLevelhead().levelCache.clear();
        });

        regSlider(new GuiSlider(6, this.width / 2 - 155, calculateHeight(5), 150, 20, "Header Red: ", "", 0, 255, LevelheadConfig.headerRed, false, true, slider -> {
            LevelheadConfig.headerRed = slider.getValueInt();
            updatePeopleToValues();
            slider.dragging = false;
        }), null);
        regSlider(new GuiSlider(7, this.width / 2 - 155, calculateHeight(6), 150, 20, "Header Green: ", "", 0, 255, LevelheadConfig.headerGreen, false, true, slider -> {
            LevelheadConfig.headerGreen = slider.getValueInt();
            updatePeopleToValues();
            slider.dragging = false;
        }), null);
        regSlider(new GuiSlider(8, this.width / 2 - 155, calculateHeight(7), 150, 20, "Header Blue: ", "", 0, 255, LevelheadConfig.headerBlue, false, true, slider -> {
            LevelheadConfig.headerBlue = slider.getValueInt();
            updatePeopleToValues();
            slider.dragging = false;
        }), null);

        regSlider(new GuiSlider(10, this.width / 2 + 5, calculateHeight(5), 150, 20, "Footer Red: ", "", 0, 255, LevelheadConfig.footerRed, false, true, slider -> {
            LevelheadConfig.footerRed = slider.getValueInt();
            updatePeopleToValues();
            slider.dragging = false;
        }), null);
        regSlider(new GuiSlider(11, this.width / 2 + 5, calculateHeight(6), 150, 20, "Footer Green: ", "", 0, 255, LevelheadConfig.footerGreen, false, true, slider -> {
            LevelheadConfig.footerGreen = slider.getValueInt();
            updatePeopleToValues();
            slider.dragging = false;
        }), null);
        regSlider(new GuiSlider(12, this.width / 2 + 5, calculateHeight(7), 150, 20, "Footer Blue: ", "", 0, 255, LevelheadConfig.footerBlue, false, true, slider -> {
            LevelheadConfig.footerBlue = slider.getValueInt();
            updatePeopleToValues();
            slider.dragging = false;
        }), null);
    }

    private void regSlider(GuiSlider slider, Consumer<GuiButton> but) {
        slider.yPosition += 30;
        reg(slider, but);
        sliders.add(slider);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float ticks) {
        lock.lock();
        drawDefaultBackground();

        drawTitle();
        drawLook();
        textField.drawTextBox();

        headerColorButton.visible = LevelheadConfig.headerChroma && !LevelheadConfig.headerRgb;
        footerColorButton.visible = LevelheadConfig.footerChroma && !LevelheadConfig.footerRgb;
        prefixButton.enabled = !textField.getText().isEmpty();
        if (LevelheadConfig.headerRgb) {
            for (GuiButton slider : sliders) {
                if (slider.displayString.contains("Header"))
                    slider.visible = true;
            }
        } else {
            for (GuiButton slider : sliders) {
                if (slider.displayString.contains("Header"))
                    slider.visible = false;
            }
        }
        if (LevelheadConfig.footerRgb) {
            for (GuiButton slider : sliders) {
                if (slider.displayString.contains("Footer")) {
                    slider.visible = true;
                }
            }
        } else {
            for (GuiButton slider : sliders) {
                if (slider.displayString.contains("Footer"))
                    slider.visible = false;
            }
        }

        for (GuiButton aButtonList : this.buttonList) {
            aButtonList.drawButton(this.mc, mouseX, mouseY);
        }
        lock.unlock();
    }

    private String getMode(boolean header) {
        if (header) {
            return LevelheadConfig.headerChroma ? "Chroma" : LevelheadConfig.headerRgb ? "RGB" : "Classic";
        } else {
            return LevelheadConfig.footerChroma ? "Chroma" : LevelheadConfig.footerRgb ? "RGB" : "Classic";
        }
    }

    private void updatePeopleToValues() {
        Levelhead levelhead = Hyperium.INSTANCE.getModIntegration().getLevelhead();

        for (Map.Entry<UUID, LevelheadTag> entry : levelhead.levelCache.entrySet()) {
            UUID uuid = entry.getKey();
            LevelheadTag levelheadTag =  entry.getValue();

            String value = levelhead.getTrueValueCache().get(uuid);
            if (value == null)
                return;
            JsonHolder footer = new JsonHolder().put("level", NumberUtils.isNumber(value) ? Long.parseLong(value) : -1).put("strlevel", value);
            LevelheadTag tag = levelhead.buildTag(footer);
            levelheadTag.reApply(tag);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        Consumer<GuiButton> guiButtonConsumer = clicks.get(button);
        if (guiButtonConsumer != null) {
            guiButtonConsumer.accept(button);
            // Adjust loaded levelhead names
            updatePeopleToValues();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1) {
            mc.displayGuiScreen(null);
        } else if (textField.isFocused() && keyCode == 28) {
            changePrefix();
        } else {
            if (Character.isLetterOrDigit(typedChar) || isCtrlKeyDown() || keyCode == 14) {
                textField.textboxKeyTyped(typedChar, keyCode);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        textField.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 0) {
            for (GuiButton guibutton : this.buttonList) {
                if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
                    guibutton.playPressSound(this.mc.getSoundHandler());
                    this.actionPerformed(guibutton);
                }
            }
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public void display() {
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(this);
    }

    @Override
    public void sendChatMessage(String msg) {
        GeneralChatHandler.instance().sendMessage(msg);
    }

    private void changePrefix() {
        if (!textField.getText().isEmpty()) {
            LevelheadConfig.customHeader = textField.getText();
            this.mod.levelCache.clear();
            sendChatMessage(String.format("Levelhead prefix is now %s!", ChatColor.GOLD + textField.getText() + ChatColor.YELLOW));
        } else {
            sendChatMessage("No prefix supplied!");
        }
        mc.displayGuiScreen(null);
    }

    private void drawTitle() {
        String text = "Levelhead for HJB";

        drawCenteredString(mc.fontRendererObj, text, this.width / 2, 5, Color.WHITE.getRGB());
        drawHorizontalLine(this.width / 2 - mc.fontRendererObj.getStringWidth(text) / 2 - 5, this.width / 2 + mc.fontRendererObj.getStringWidth(text) / 2 + 5, 15, Color.WHITE.getRGB());
        drawCenteredString(mc.fontRendererObj, ChatColor.YELLOW + "Custom Levelhead Status: " + (isCustom ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled / Inactive"), this.width / 2,
            20, Color.WHITE.getRGB());
    }

    private void drawLook() {
        FontRenderer renderer = mc.fontRendererObj;
        if (LevelheadConfig.ENABLED) {
            drawCenteredString(renderer, "This is how levels will display", this.width / 2, 30, Color.WHITE.getRGB());
            LevelheadTag levelheadTag = Hyperium.INSTANCE.getModIntegration().getLevelhead().buildTag(new JsonHolder());
            LevelheadComponent header = levelheadTag.getHeader();
            int h = 40;
            if (header.isChroma())
                drawCenteredString(renderer, header.getValue(), this.width / 2, h, Hyperium.INSTANCE.getModIntegration().getLevelhead().getRGBColor());
            else if (header.isRgb()) {
                drawCenteredString(renderer, header.getValue(), this.width / 2, h, new Color(header.getRed(), header.getGreen(), header.getBlue(), header.getAlpha()).getRGB());

            } else {
                drawCenteredString(renderer, header.getColor() + header.getValue(), this.width / 2, h, Color.WHITE.getRGB());
            }

            LevelheadComponent footer = levelheadTag.getFooter();
            footer.setValue("5");
            if (footer.isChroma())
                drawCenteredString(renderer, footer.getValue(), (this.width / 2 + renderer.getStringWidth(header.getValue()) / 2 + 3), h, Hyperium.INSTANCE.getModIntegration().getLevelhead().getRGBColor());
            else if (footer.isRgb()) {
                drawCenteredString(renderer, footer.getValue(), (this.width / 2 + renderer.getStringWidth(header.getValue()) / 2 + 3), h, new Color(footer.getRed(), footer.getBlue(), footer.getGreen(), footer.getAlpha()).getRGB());
            } else {
                drawCenteredString(renderer, footer.getColor() + footer.getValue(), (this.width / 2 + renderer.getStringWidth(header.getValue()) / 2 + 3), h, Color.WHITE.getRGB());
            }

        } else {
            drawCenteredString(renderer, "Levelhead disabled", this.width / 2, 30, Color.WHITE.getRGB());
            drawCenteredString(renderer, "Player levels will not appear", this.width / 2, 40, Color.WHITE.getRGB());
        }
    }

    private String removeColorChar(String message) {
        return message.replace(COLOR_CHAR, "");
    }
}
