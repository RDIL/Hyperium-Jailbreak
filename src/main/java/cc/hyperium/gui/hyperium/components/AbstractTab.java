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
import java.util.stream.Collectors;

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
        ScaledResolution sr = ResolutionUtil.current();
        int sw = sr.getScaledWidth();
        int sh = sr.getScaledHeight();
        int xg = width / 9;   // X grid

        /* Begin new scissor state */
        ScissorState.scissor(x, y, width, height, true);

        /* Get mouse X and Y */
        final int mx = Mouse.getX() * sw / Minecraft.getMinecraft().displayWidth;           // Mouse X
        final int my = sh - Mouse.getY() * sh / Minecraft.getMinecraft().displayHeight - 1; // Mouse Y

        if (scrollAnim.getValue() != scroll * 18 && scrollAnim.isFinished())
            scrollAnim = new SimpleAnimValue(1000L, scrollAnim.getValue(), scroll * 18);
        y += scrollAnim.getValue();
        /* Render each tab component */
        for (AbstractTabComponent comp : components) {
            comp.render(x, y, width, mx, my);

            /* If mouse is over component, set as hovered */
            if (mx >= x && mx <= x + width && my > y && my <= y + comp.getHeight()) {
                comp.hover = true;
                //For slider
                comp.mouseEvent(mx - xg, my - y /* Make the Y relevant to the component */);
                if (Mouse.isButtonDown(0)) {
                    if (!clickStates.computeIfAbsent(comp, ignored -> false)) {
                        comp.onClick(mx, my - y /* Make the Y relevant to the component */);
                        clickStates.put(comp, true);
                    }
                } else if (clickStates.computeIfAbsent(comp, ignored -> false))
                    clickStates.put(comp, false);
            } else
                comp.hover = false;
            y += comp.getHeight();
        }

        /* End scissor state */
        ScissorState.endScissor();
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
