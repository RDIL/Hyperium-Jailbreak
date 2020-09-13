package cc.hyperium.mixinsimp.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.GuiHyperiumScreenMainMenu;
import net.minecraft.client.gui.GuiMainMenu;

public class HyperiumGuiMainMenu {
    private final GuiMainMenu parent;

    public HyperiumGuiMainMenu(GuiMainMenu parent) {
        this.parent = parent;
    }

    public void initGui() {
        parent.drawDefaultBackground();
    }

    public void drawScreen() {
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiHyperiumScreenMainMenu());
    }
}
