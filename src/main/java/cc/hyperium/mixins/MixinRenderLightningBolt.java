package cc.hyperium.mixins;

import cc.hyperium.config.provider.OptimizationOptionsProvider;
import net.minecraft.client.renderer.entity.RenderLightningBolt;
import net.minecraft.entity.effect.EntityLightningBolt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderLightningBolt.class)
public class MixinRenderLightningBolt {
    @Inject(method = "doRender", at = @At("HEAD"), cancellable = true)
    public void doRender(EntityLightningBolt entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (OptimizationOptionsProvider.DISABLE_LIGHTNING) {
            ci.cancel();
        }
    }
}
