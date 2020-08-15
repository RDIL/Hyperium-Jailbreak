package cc.hyperium.mods.chromahud.displayitems.hyperium.handlers;

import java.util.List;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.entity.PlayerAttackEntityEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.AxisAlignedBB;

public class ZealotHandler {
    public static ZealotHandler INSTANCE = new ZealotHandler();
    private int slainZealots = 0;

    @InvokeEvent
    public void onEntityKilled(PlayerAttackEntityEvent event) {
        if (event.getEntity().isDead && Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel()) {
            if (isZealot(event.getEntity())) {
                slainZealots++;
            }
        }
    }

    public boolean isZealot(Entity enderman) {
        List<EntityArmorStand> stands = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityArmorStand.class, new AxisAlignedBB(enderman.posX - 1, enderman.posY, enderman.posZ - 1, enderman.posX + 1, enderman.posY + 5, enderman.posZ + 1));
        if (stands.isEmpty()) return false;

        EntityArmorStand armorStand = stands.get(0);
        return armorStand.hasCustomName() && armorStand.getCustomNameTag().contains("Zealot");
    }

    public int getSlainZealots() {
        return slainZealots;
    }
}
