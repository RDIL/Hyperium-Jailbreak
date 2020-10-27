package cc.hyperium.event.world;

import cc.hyperium.event.Event;
import net.minecraft.world.chunk.Chunk;

public class ChunkLoadEvent extends Event {
    private final Chunk chunk;

    public ChunkLoadEvent(Chunk chunk) {
        this.chunk = chunk;
    }

    public Chunk getChunk() {
        return this.chunk;
    }
}
