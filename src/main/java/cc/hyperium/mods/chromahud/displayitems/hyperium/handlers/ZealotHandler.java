package cc.hyperium.mods.chromahud.displayitems.hyperium.handlers;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.entity.PlayerAttackEntityEvent;
import net.minecraft.entity.monster.EntityEnderman;

public class ZealotHandler {
    public static ZealotHandler INSTANCE = new ZealotHandler();
    private int slainZealots = 0;

    @InvokeEvent
    public void onEntityKilled(PlayerAttackEntityEvent event) {
        if (event.getEntity().isDead && Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel()) {
            if (event.getEntity() instanceof EntityEnderman) {
                if (event.getEntity().getName().toLowerCase().equals("zealot")) {
                    slainZealots++;
                }
            }
        }
    }

    public int getSlainZealots() {
        return slainZealots;
    }
}
