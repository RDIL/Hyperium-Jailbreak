package cc.hyperium.mods.autotext.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import org.lwjgl.input.Keyboard;

public class ConfirmGui extends GuiYesNo {
    public ConfirmGui(int keyTyped) {
        super(getYesNoCallback(keyTyped), "You pressed " + Keyboard.getKeyName(keyTyped) + ", is this the key you want?", "", 0);
    }

    static GuiYesNoCallback getYesNoCallback(final int keyTyped) {
        return (result, id) -> {
            if (result) {
                Minecraft.getMinecraft().displayGuiScreen(new EnterCommandGui(keyTyped));
            } else {
                Minecraft.getMinecraft().displayGuiScreen(null);
            }
        };
    }
}
