package cc.hyperium.mixinsimp.gui;

import cc.hyperium.gui.hyperium.HyperiumMainGui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import java.util.List;

public class HyperiumGuiOptions {
    private GuiOptions parent;

    public HyperiumGuiOptions(GuiOptions parent) {
        this.parent = parent;
    }

    public void initGui(List<GuiButton> buttonList) {
        buttonList.forEach(b -> {
            if (b.id == 200) b.yPosition = parent.height - 30;
            if (b.id == 104) b.displayString = "Hyperium Settings...";
        });
    }

    public void actionPerformed(GuiButton button) {
        if (button.id == 104) HyperiumMainGui.INSTANCE.show();
    }
}
