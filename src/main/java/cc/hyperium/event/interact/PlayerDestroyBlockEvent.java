package cc.hyperium.event.interact;

import cc.hyperium.event.CancellableEvent;
import net.minecraft.util.BlockPos;

public class PlayerDestroyBlockEvent extends CancellableEvent {
    public final BlockPos blockPos;

    public PlayerDestroyBlockEvent(BlockPos blockPos) {
        this.blockPos = blockPos;
    }
}
