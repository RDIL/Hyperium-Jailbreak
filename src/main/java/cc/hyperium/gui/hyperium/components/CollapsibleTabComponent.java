package cc.hyperium.gui.hyperium.components;

import cc.hyperium.gui.Icons;
import cc.hyperium.mixins.gui.IMixinGuiScreen;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class CollapsibleTabComponent extends AbstractTabComponent {
    private List<AbstractTabComponent> children = new ArrayList<>();
    private boolean collapsed = true;
    private String name;
    private CollapsibleTabComponent parent;

    public CollapsibleTabComponent(String name) {
        super();
        this.name = name;
    }

    public CollapsibleTabComponent getParent() {
        return parent;
    }

    public void setParent(CollapsibleTabComponent parent) {
        this.parent = parent;
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

        tab.gui.drawString(((IMixinGuiScreen) tab.gui).getFontRendererObj(), label.replaceAll("_", " ").toUpperCase(), x + 3, y + 5, 0xffffff);

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

        boolean right = false;
        int prevH = 0;

        for (AbstractTabComponent comp : children) {
            if (parent != null) right = false;

            comp.render(right ? x + width / 2 : x, y, parent != null ? width : width / 2, mouseX, mouseY);

            if (mouseX >= (right ? x + width / 2 : x) && mouseX <= (right ? x + width / 2 : x) + (parent != null ? width : width / 2) && mouseY >= y && mouseY <= y + comp.getHeight()) {
                comp.hover = true;
                comp.mouseEvent(right ? mouseX - width / 2 - x : mouseX - x, mouseY - y /* Make the Y relevant to the component */);

                if (Mouse.isButtonDown(0)) {
                    if (!tab.clickStates.computeIfAbsent(comp, ignored -> false)) {
                        comp.onClick(right ? mouseX - width / 2 : mouseX,
                            mouseY - y /* Make the Y relevant to the component */);
                        tab.clickStates.put(comp, true);
                    }
                } else if (tab.clickStates.computeIfAbsent(comp, ignored -> false)) {
                    tab.clickStates.put(comp, false);
                }
            } else {
                comp.hover = false;
            }

            boolean b = right || parent != null;
            if (b) y += Math.max(comp.getHeight(), prevH);
            right = !right;

            prevH = comp.getHeight();
        }
    }

    @Override
    public int getHeight() {
        if (collapsed) {
            return 18;
        } else {
            if (parent != null) {
                int h = 18;
                for (AbstractTabComponent child : this.children) {
                    h += child.getHeight();
                }
                return h;
            }

            Iterator<AbstractTabComponent> iterator = children.iterator();
            boolean right = true;
            int leftHeight = 0;
            int compH = 18;
            while (iterator.hasNext()) {
                right = !right;
                AbstractTabComponent next = iterator.next();
                int height = next.getHeight();
                if (right) {
                    compH += Math.max(leftHeight, height);
                    leftHeight = 0;
                } else {
                    leftHeight = height;
                }
            }
            compH += leftHeight;
            return compH;
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

    public void sortSelf() {
        children.sort(Comparator.comparing(this::getLabel));
    }

    private String getLabel(AbstractTabComponent component) {
        if (component instanceof CollapsibleTabComponent) {
            return ((CollapsibleTabComponent) component).getLabel();
        }
        if (component instanceof SliderComponent) {
            return ((SliderComponent) component).getLabel();
        }
        if (component instanceof ToggleComponent)
            return ((ToggleComponent) component).getLabel();
        if (component instanceof SelectorComponent)
            return ((SelectorComponent) component).getLabel();
        return "No Title";
    }
}
