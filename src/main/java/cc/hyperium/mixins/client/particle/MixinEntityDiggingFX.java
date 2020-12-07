package cc.hyperium.mixins.client.particle;

import cc.hyperium.config.Settings;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityDiggingFX.class)
public class MixinEntityDiggingFX {
    @Inject(method = "renderParticle", at = @At("HEAD"), cancellable = true)
    public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_, CallbackInfo ci) {
        if (!Settings.BLOCK_BREAK_PARTICLES) {
            ci.cancel();
        }
    }
}
