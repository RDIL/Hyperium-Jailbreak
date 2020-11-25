package cc.hyperium.event.gui.container;

import cc.hyperium.event.CancellableEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

/**
 * Called before {@code checkHotbarKeys} in GuiContainer.
 *
 * Warning: please don't use this unless you know what you are doing. This event has
 * been added for a very, very specific use case.
 */
public class GuiContainerKeyTypedPreHBKeysEvent extends CancellableEvent {
    public final GuiContainer container;
    public final int keyCode;
    public final Slot theSlot;

    public GuiContainerKeyTypedPreHBKeysEvent(GuiContainer container, int keyCode, Slot theSlot) {
        this.container = container;
        this.keyCode = keyCode;
        this.theSlot = theSlot;
    }
}
