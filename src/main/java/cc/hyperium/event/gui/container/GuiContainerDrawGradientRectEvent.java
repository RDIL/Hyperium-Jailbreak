package cc.hyperium.event.gui.container;

import cc.hyperium.event.Event;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

/**
 * Called before {@code Gui#drawGradientRect(int, int, int, int, int, int)} for GUI containers.
 *
 * Warning: please don't use this unless you know what you are doing. This event has
 * been added for a very, very specific use case.
 */
public class GuiContainerDrawGradientRectEvent extends Event {
    public final GuiContainer container;
    public final int left;
    public final int top;
    public final int right;
    public final int bottom;
    public final int startColor;
    public final int endColor;
    public final Slot theSlot;

    public GuiContainerDrawGradientRectEvent(GuiContainer container, int left, int top, int right, int bottom, int startColor, int endColor, Slot theSlot) {
        this.container = container;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.startColor = startColor;
        this.endColor = endColor;
        this.theSlot = theSlot;
    }
}
