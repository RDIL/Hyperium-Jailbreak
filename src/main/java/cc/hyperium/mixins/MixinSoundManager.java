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

package cc.hyperium.mixins;

import cc.hyperium.mixinsimp.HyperiumSoundManager;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.audio.ISound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundManager.class)
public class MixinSoundManager {
    private HyperiumSoundManager hyperiumSoundManager = new HyperiumSoundManager();

    @Inject(method = "playSound",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/audio/SoundHandler;getSound(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/audio/SoundEventAccessorComposite;"),
        cancellable = true
    )
    private void playSound(ISound sound, CallbackInfo ci) {
        hyperiumSoundManager.playSound(sound, ci);
    }

    @Inject(method = "updateAllSounds", at = @At("HEAD"))
    private void startUpdate(CallbackInfo info) {
        hyperiumSoundManager.startUpdate(info);
    }

    @Inject(method = "updateAllSounds", at = @At("TAIL"))
    private void endUpdate(CallbackInfo info) {
        hyperiumSoundManager.endUpdate(info);
    }

    @Inject(method = "stopAllSounds", at = @At("HEAD"))
    private void startStopAllSounds(CallbackInfo info) {
        hyperiumSoundManager.startStopAllSounds(info);
    }

    @Inject(method = "stopAllSounds", at = @At("TAIL"))
    private void endStopAllSounds(CallbackInfo info) {
        hyperiumSoundManager.endStopAllSounds(info);
    }
}
