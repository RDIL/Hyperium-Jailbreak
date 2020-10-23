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

import java.util.Iterator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cc.hyperium.Hyperium;
import cc.hyperium.mixinsimp.HyperiumSoundManager;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundManager;

@Mixin(SoundManager.class)
public abstract class MixinSoundManager {
    @Inject(
        method = "playSound",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/audio/SoundHandler;getSound(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/audio/SoundEventAccessorComposite;"),
        cancellable = true
    )
    private void playSound(ISound sound, CallbackInfo ci) {
        HyperiumSoundManager.playSound(sound, ci);
    }

    @SuppressWarnings("all")
    @Redirect(at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z"), method = "updateAllSounds")
    private boolean iteratorFix(Iterator iter) {
        Object hasNext = null;
        while (hasNext == null) {
            try {
                if (iter.hasNext()) {
                    hasNext = true;
                }
                hasNext = false;
            } catch (Exception e) {
                Hyperium.LOGGER.debug("ConcurrentModificationException thrown while trying to iterate over sounds, trying again...");
            }
        }
        return (boolean) hasNext;
    }
}
