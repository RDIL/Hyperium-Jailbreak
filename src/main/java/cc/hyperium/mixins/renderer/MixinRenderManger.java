package cc.hyperium.mixins.renderer;

import cc.hyperium.config.Settings;
import com.hyperiumjailbreak.SkyblockModPort;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderManager.class)
public class MixinRenderManger {
    // taken from skyblock addons
    // https://github.com/biscuut/skyblockaddons
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void shouldRender(Entity entityIn, ICamera camera, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
        if (Settings.hide_skeletonhat_bones && entityIn instanceof EntityItem && entityIn.ridingEntity instanceof EntityArmorStand && entityIn.ridingEntity.isInvisible() && SkyblockModPort.playing()) {
            cir.setReturnValue(false);
        }
    }
}

