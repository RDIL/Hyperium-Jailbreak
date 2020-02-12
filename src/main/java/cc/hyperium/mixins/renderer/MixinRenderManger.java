package cc.hyperium.mixins.renderer;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.cosmetics.companions.hamster.EntityHamster;
import cc.hyperium.cosmetics.companions.hamster.RenderHamster;
import cc.hyperium.mixinsimp.renderer.IMixinRenderManager;

import com.hyperiumjailbreak.SkyblockAddonsData;
import com.hyperiumjailbreak.SkyblockModPort;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Map;

@Mixin(RenderManager.class)
public class MixinRenderManger implements IMixinRenderManager {
    @Shadow
    private double renderPosX;

    @Shadow
    private double renderPosY;

    @Shadow
    private double renderPosZ;

    @Shadow
    private Map<Class<? extends Entity>, Render<? extends Entity>> entityRenderMap;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectEntities(TextureManager renderEngineIn, RenderItem itemRendererIn, CallbackInfo ci) {
        this.entityRenderMap.put(EntityHamster.class, new RenderHamster((RenderManager) (Object) this));
    }

    @Override
    public double getPosX() {
        return renderPosX;
    }

    @Override
    public double getPosY() {
        return renderPosY;
    }

    @Override
    public double getPosZ() {
        return renderPosZ;
    }

    // taken from skyblock addons
    // https://github.com/biscuut/skyblockaddons
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void shouldRender(Entity entityIn, ICamera camera, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
        // skeleton hat bones
        if (Settings.hide_skeletonhat_bones && entityIn instanceof EntityItem && entityIn.ridingEntity instanceof EntityArmorStand && entityIn.ridingEntity.isInvisible() && SkyblockModPort.playing()) {
            cir.setReturnValue(false);
        }

        if (Settings.HIDE_AH_PLAYERS && entityIn instanceof EntityOtherPlayerMP) {
            if (SkyblockAddonsData.SkyblockNPC.isNearNPC(entityIn)) {
                cir.setReturnValue(false);
            }
        }

        if (Settings.SB_HIDE_PLAYERS_IN_LOBBY) {
            SkyblockAddonsData.Location loc = Hyperium.INSTANCE.getUtils().getLocation();
            if (loc == SkyblockAddonsData.Location.VILLAGE || loc == SkyblockAddonsData.Location.AUCTION_HOUSE || loc == SkyblockAddonsData.Location.BANK) {
                if ((entityIn instanceof EntityOtherPlayerMP || entityIn instanceof EntityFX || entityIn instanceof EntityItemFrame) && entityIn.getDistanceToEntity(Minecraft.getMinecraft().thePlayer) > 7) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}

