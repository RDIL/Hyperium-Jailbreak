package cc.hyperium.event.entity;

import cc.hyperium.event.Event;
import net.minecraft.entity.Entity;

/**
 * An event for when the player's hurt time is reset.
 */
public class PlayerResetHurtTimeEvent extends Event {
    /**
     * The entity.
     */
    public final Entity entity;

    /**
     * Create a new instance of the event with the specified entity.
     *
     * @param entity The entity.
     */
    public PlayerResetHurtTimeEvent(Entity entity) {
        this.entity = entity;
    }
}
