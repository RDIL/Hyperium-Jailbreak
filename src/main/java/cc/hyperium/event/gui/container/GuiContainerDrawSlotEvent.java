package cc.hyperium.event.gui.container;

import cc.hyperium.event.Event;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

public class GuiContainerDrawSlotEvent extends Event {
    public final GuiContainer container;
    public final Slot theSlot;

    public GuiContainerDrawSlotEvent(GuiContainer container, Slot theSlot) {
        this.container = container;
        this.theSlot = theSlot;
    }
}
