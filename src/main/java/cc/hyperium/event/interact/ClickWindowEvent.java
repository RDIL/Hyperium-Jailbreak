package cc.hyperium.event.interact;

import cc.hyperium.event.CancellableEvent;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Called when the player clicks on the window, namely an inventory slot.
 */
public class ClickWindowEvent extends CancellableEvent {
    public final int windowId;
    public final int slotId;
    public final int mouseButtonClicked;
    public final int mode;
    public final EntityPlayer playerIn;

    public ClickWindowEvent(int windowId, int slotId, int mouseButtonClicked, int mode, EntityPlayer playerIn) {
        this.windowId = windowId;
        this.slotId = slotId;
        this.mouseButtonClicked = mouseButtonClicked;
        this.mode = mode;
        this.playerIn = playerIn;
    }
}
