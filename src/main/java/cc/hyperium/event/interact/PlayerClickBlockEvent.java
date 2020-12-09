package cc.hyperium.event.interact;

import cc.hyperium.event.CancellableEvent;
import net.minecraft.util.BlockPos;

/**
 * An event triggered when the player clicks a block, or is holding the mouse down while hovering over a block.
 */
public class PlayerClickBlockEvent extends CancellableEvent {
    public final BlockPos blockPos;

    public PlayerClickBlockEvent(BlockPos blockPos) {
        this.blockPos = blockPos;
    }
}
