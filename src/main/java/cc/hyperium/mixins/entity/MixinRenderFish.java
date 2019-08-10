package cc.hyperium.mixins.entity;

import net.minecraft.client.renderer.entity.RenderFish;
import net.minecraft.entity.projectile.EntityFishHook;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderFish.class)
public class MixinRenderFish {
    @Inject(method = "doRender", at = @At("HEAD"))
    public void doRender(EntityFishHook entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        GL11.glLineWidth(1.0F); // Set line width to normal to prevent becoming thick
    }
}
