package cc.hyperium.mods.autotext.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.mods.autotext.AutoText;
import cc.hyperium.mods.autotext.config.AutoTextConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

public class EnterCommandGui extends GuiScreen {
    private final int keyTyped;
    private GuiTextField guiTextField;

    public EnterCommandGui(int keyTyped) {
        this.keyTyped = keyTyped;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        guiTextField = new GuiTextField(0, fontRendererObj, width / 2 - 50, height / 2, 100, 15);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawWorldBackground(3);
        guiTextField.drawTextBox();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        guiTextField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_RETURN) {
            AutoTextConfig.INSTANCE.getKeybinds().put(String.valueOf(keyTyped), guiTextField.getText());
            Hyperium.CONFIG.save();
            Hyperium.INSTANCE.getHandlers().getKeybindHandler().registerKeyBinding(new HyperiumBind(guiTextField.getText(), keyTyped) {
                @Override
                public void onPress() {
                    Minecraft.getMinecraft().thePlayer.sendChatMessage(guiTextField.getText());
                }
            });
            Minecraft.getMinecraft().displayGuiScreen(null);
        } else {
            guiTextField.textboxKeyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
    }
}
