package cc.hyperium.gui.hyperium.components;

import cc.hyperium.gui.ScissorState;
import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.SimpleAnimValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractTab {
    protected List<AbstractTabComponent> components = new ArrayList<>();
    Map<AbstractTabComponent, Boolean> clickStates = new HashMap<>();
    protected HyperiumMainGui gui;
    private String title;
    private SimpleAnimValue scrollAnim = new SimpleAnimValue(0L, 0f, 0f);
    private int scroll = 0;

    public AbstractTab(HyperiumMainGui gui, String title) {
        this.gui = gui;
        this.title = title;
    }

    public void render(int x, int y, int width, int height) {
    }

    public String getTitle() {
        return this.title;
    }

    public void handleMouseInput() {
        if (Mouse.getEventDWheel() > 0)
            scroll++;
        else if (Mouse.getEventDWheel() < 0)
            scroll--;
        if (scroll > 0)
            scroll = 0;
    }
}
