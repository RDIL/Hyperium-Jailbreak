package cc.hyperium.mixins.renderer;

import cc.hyperium.event.EntityRenderEvent;
import cc.hyperium.event.EventBus;
import cc.hyperium.mixinsimp.renderer.HyperiumRender;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Render.class)
public abstract class MixinRender<T extends Entity> {
    @Shadow @Final private RenderManager renderManager;
    private HyperiumRender<T> hyperiumRender = new HyperiumRender<>((Render<T>) (Object) this);

    @Overwrite
    protected void renderOffsetLivingLabel(T entityIn, double x, double y, double z, String str, float p_177069_9_, double p_177069_10_) {
        hyperiumRender.renderOffsetLivingLabel(entityIn, x, y, z, str);
    }

    @Overwrite
    protected void renderName(T entity, double x, double y, double z) {
        hyperiumRender.renderName(entity, x, y, z);
    }

    @Overwrite
    protected void renderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance) {
        hyperiumRender.renderLivingLabel(entityIn, str, x, y, z, maxDistance, renderManager);
    }

    @Inject(method = "doRender", at = @At("HEAD"))
    private void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo callbackInfo) {
        EventBus.INSTANCE.post(new EntityRenderEvent(entity, (float) x, (float) y, (float) z, entity.rotationPitch, entityYaw, 1.0F));
    }
}

