package cc.hyperium.event.entity;

import cc.hyperium.event.Event;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityJoinWorldEvent extends Event {
    private final World world;
    private final Entity entity;

    public EntityJoinWorldEvent(Entity entity, World world) {
        this.entity = entity;
        this.world = world;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public World getWorld() {
        return this.world;
    }
}
