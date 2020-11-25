package cc.hyperium.event.gui.container;

import cc.hyperium.event.Event;
import net.minecraft.client.gui.inventory.GuiContainer;

/**
 * Called before the tail of drawScreen on GuiContainer.
 *
 * Warning: please don't use this unless you know what you are doing. This event has
 * been added for a very, very specific use case.
 */
public class GuiContainerFinishDrawScreenEvent extends Event {
    public final GuiContainer container;
    public final int mouseX;
    public final int mouseY;
    public final float partialTicks;

    public GuiContainerFinishDrawScreenEvent(GuiContainer container, int mouseX, int mouseY, float partialTicks) {
        this.container = container;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.partialTicks = partialTicks;
    }
}
