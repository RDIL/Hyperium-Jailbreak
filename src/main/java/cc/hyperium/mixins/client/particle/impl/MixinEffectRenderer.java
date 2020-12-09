package cc.hyperium.mixins.client.particle.impl;

import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EffectRenderer.class)
public class MixinEffectRenderer {
    @Inject(method = "tickParticle", at = @At("HEAD"), cancellable = true)
    private void tickParticle(EntityFX p_178923_1_, CallbackInfo ci) {
        if (p_178923_1_ == null) {
            ci.cancel();
        }
    }
}
