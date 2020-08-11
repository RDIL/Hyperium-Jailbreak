package cc.hyperium.mods.autotext.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ConfirmGui extends GuiScreen {
    private final int keyTyped;

    public ConfirmGui(int keyTyped) {
        this.keyTyped = keyTyped;
    }

    @Override
    public void initGui() {
        buttonList.add(new GuiButton(0, width / 2 - 60, height / 2 + 22, 50, 10, "Yes"));
        buttonList.add(new GuiButton(1, width / 2, height / 2 + 22, 50, 10, "No"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawWorldBackground(3);
        drawCenteredString(fontRendererObj, "You chose " + Keyboard.getKeyName(keyTyped) + ", to confirm it press Yes. To exit press No.", width / 2, height / 2, -1);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            Minecraft.getMinecraft().displayGuiScreen(new EnterCommandGui(keyTyped));
        } else {
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }
}
