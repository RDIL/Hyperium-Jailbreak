package cc.hyperium.gui.hyperium.components;

import cc.hyperium.gui.Icons;
import cc.hyperium.gui.hyperium.HyperiumSettingsGui;
import cc.hyperium.mixins.gui.IMixinGuiScreen;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CollapsibleTabComponent extends AbstractTabComponent {
    private List<AbstractTabComponent> children = new ArrayList<>();
    private boolean collapsed = true;
    private final String name;
    private final HyperiumSettingsGui gui;

    public CollapsibleTabComponent(HyperiumSettingsGui gui, String name) {
        super();
        this.gui = gui;
        this.name = name;
    }

    public String getLabel() {
        return name;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    public List<AbstractTabComponent> getChildren() {
        return children;
    }

    @Override
    public void render(int x, int y, int width, int mouseX, int mouseY) {
        super.render(x, y, width, mouseX, mouseY);

        gui.drawString(((IMixinGuiScreen) gui).getFontRendererObj(), name.replaceAll("_", " ").toUpperCase(), x + 3, y + 5, 0xffffff);

        GlStateManager.bindTexture(0);

        if (collapsed) {
            Icons.ARROW_UP_ALT.bind();
        } else {
            Icons.ARROW_DOWN_ALT.bind();
        }

        Gui.drawScaledCustomSizeModalRect(x + width - 20, y, 0, 0, 144, 144, 20, 20, 144, 144);

        if (collapsed) return;

        y += 18;
        x += 10;
        width -= 10;

        for (AbstractTabComponent comp : children) {
            comp.render(x, y, width / 2, mouseX, mouseY);

            if (mouseX >= x && mouseX <= x + (width / 2) && mouseY >= y && mouseY <= y + comp.getHeight()) {
                comp.hover = true;
                comp.mouseEvent(mouseX - x, mouseY - y);

                if (Mouse.isButtonDown(0)) {
                    if (!gui.clickStates.computeIfAbsent(comp, ignored -> false)) {
                        comp.onClick(mouseX, mouseY - y);
                        gui.clickStates.put(comp, true);
                    }
                } else if (gui.clickStates.computeIfAbsent(comp, ignored -> false)) {
                    gui.clickStates.put(comp, false);
                }
            } else {
                comp.hover = false;
            }
        }
    }

    @Override
    public int getHeight() {
        if (collapsed) {
            return 18;
        } else {
            final Iterator<AbstractTabComponent> iterator = children.iterator();
            int height = 0;
            while (iterator.hasNext()) {
                AbstractTabComponent next = iterator.next();
                height = next.getHeight();
            }
            return 18 + height;
        }
    }

    public CollapsibleTabComponent addChild(AbstractTabComponent component) {
        children.add(component);
        return this;
    }

    @Override
    public void onClick(int x, int y) {
        if (y < 18) {
            collapsed = !collapsed;
        }
    }
}
