package cc.hyperium.mods.autotext.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class SetBindGui extends GuiScreen {
    private int keyTyped;
    private boolean isTyped;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawWorldBackground(3);
        if (!isTyped) {
            drawCenteredString(fontRendererObj, "Press the key to use", width / 2, height / 2, -1);
        } else {
            Minecraft.getMinecraft().displayGuiScreen(makeConfirm(keyTyped));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        keyTyped = keyCode;
        isTyped = true;
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    private GuiYesNo makeConfirm(final int keyTyped) {
        return new GuiYesNo(getConfirmCallback(keyTyped), "You pressed " + Keyboard.getKeyName(keyTyped) + ", is this the key you want?", "", 0);
    }

    private GuiYesNoCallback getConfirmCallback(final int keyTyped) {
        return (result, id) -> {
            if (result) {
                Minecraft.getMinecraft().displayGuiScreen(new EnterCommandGui(keyTyped));
            } else {
                Minecraft.getMinecraft().displayGuiScreen(null);
            }
        };
    }
}
