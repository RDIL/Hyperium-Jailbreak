package cc.hyperium.mods.autotext.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

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
            Minecraft.getMinecraft().displayGuiScreen(new ConfirmGui(keyTyped));
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
        return false;
    }
}
