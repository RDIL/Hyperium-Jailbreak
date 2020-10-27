package cc.hyperium.event.entity;

import cc.hyperium.event.Event;
import net.minecraft.entity.Entity;
import net.minecraft.world.chunk.Chunk;

public class EntityEnterChunkEvent extends Event {
    private final Entity entity;
    private final Chunk chunk;

    public EntityEnterChunkEvent(Entity entity, Chunk chunk) {
        this.entity = entity;
        this.chunk = chunk;
    }

    public Entity getEntity() {
        return this.entity;
    }
    
    public Chunk getChunk() {
        return this.chunk;
    }
}
