package cc.hyperium.mixinsimp.entity;

import cc.hyperium.config.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;

public class HyperiumEntityPlayerSP {
    public HyperiumEntityPlayerSP() {}

    public void onEnchantmentCritical(Entity entityHit, Minecraft mc) {
        if (mc.isSingleplayer() || !Settings.CRIT_FIX) {
            mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT_MAGIC);
        }
    }

    public void onCriticalHit(Entity entityHit, Minecraft mc) {
        if (Minecraft.getMinecraft().isSingleplayer() || !Settings.CRIT_FIX) {
            mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT);
        }
    }
}
