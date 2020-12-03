/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.entity;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.world.RenderWorldFinalPassEvent;
import cc.hyperium.mixinsimp.entity.HyperiumEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {
    @Shadow private float thirdPersonDistance;
    @Shadow private float thirdPersonDistanceTemp;
    @Shadow private Minecraft mc;
    private HyperiumEntityRenderer hyperiumEntityRenderer = new HyperiumEntityRenderer((EntityRenderer) (Object) this);
    @Shadow @Final public static int shaderCount;
    @Shadow private int shaderIndex;

    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void updateCameraAndRender(float partialTicks, long nano, CallbackInfo ci) {
        hyperiumEntityRenderer.updateCameraAndRender();
    }

    @Inject(method = "activateNextShader", at = @At("INVOKE_ASSIGN"))
    private void activateNextShader(CallbackInfo callbackInfo) {
        HyperiumEntityRenderer.INSTANCE.isUsingShader = this.shaderIndex != shaderCount;
    }

    /**
     * @author hyperium
     */
    @Overwrite
    private void orientCamera(float partialTicks) {
        hyperiumEntityRenderer.orientCamera(partialTicks, this.thirdPersonDistanceTemp, this.thirdPersonDistance, this.mc);
    }

    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", args = "ldc=outline"))
    public void drawOutline(int pass, float part, long nano, CallbackInfo info) {
        hyperiumEntityRenderer.drawOutline(part, this.mc);
    }

    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", args = "ldc=hand"))
    public void renderWorldLastPass(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
        mc.mcProfiler.startSection("hyperium_render_last");
        EventBus.INSTANCE.post(new RenderWorldFinalPassEvent(partialTicks));
        mc.mcProfiler.endSection();
    }

    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V", args = "ldc=mouse"))
    private void updateCameraAndRender2(float partialTicks, long nanoTime, CallbackInfo ci) {
        hyperiumEntityRenderer.updatePerspectiveCamera();
    }

    @Inject(method = "getNightVisionBrightness", at = @At("HEAD"), cancellable = true)
    private void preventBlink(EntityLivingBase p_getNightVisionBrightness_1_, float p_getNightVisionBrightness_2_, CallbackInfoReturnable<Float> cir) {
        if (!Settings.NIGHT_VISION_BLINKING) {
            cir.setReturnValue(1.0F);
        }
    }
}
