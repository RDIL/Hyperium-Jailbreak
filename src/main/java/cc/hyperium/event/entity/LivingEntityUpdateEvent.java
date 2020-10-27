package cc.hyperium.event.entity;

import cc.hyperium.event.Event;
import net.minecraft.entity.EntityLivingBase;

public class LivingEntityUpdateEvent extends Event {
    private final EntityLivingBase base;

    public LivingEntityUpdateEvent(EntityLivingBase base) {
        this.base = base;
    }

    public EntityLivingBase getLivingBase() {
        return this.base;
    }
}
