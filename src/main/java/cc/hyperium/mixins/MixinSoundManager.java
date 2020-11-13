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

import cc.hyperium.mixinsimp.client.IReloadableSoundManager;
import com.google.common.collect.Multimap;
import net.minecraft.client.audio.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cc.hyperium.mixinsimp.HyperiumSoundManager;

import java.util.List;
import java.util.Map;

@Mixin(SoundManager.class)
public abstract class MixinSoundManager implements IReloadableSoundManager {
    @Shadow @Final private Map<String, ISound> playingSounds;
    @Shadow @Final private Map<String, ISound> invPlayingSounds;
    @Shadow private Map<ISound, SoundPoolEntry> playingSoundPoolEntries;
    @Shadow @Final private Multimap<SoundCategory, String> categorySounds;
    @Shadow @Final private List<ITickableSound> tickableSounds;
    @Shadow @Final private Map<ISound, Integer> delayedSounds;
    @Shadow @Final private Map<String, Integer> playingSoundsStopTime;

    @Inject(
        method = "playSound",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/audio/SoundHandler;getSound(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/audio/SoundEventAccessorComposite;"),
        cancellable = true
    )
    private void playSound(ISound sound, CallbackInfo ci) {
        HyperiumSoundManager.playSound(sound, ci);
    }

    @Override
    public void clearAll() {
        playingSounds.clear();
        invPlayingSounds.clear();
        playingSoundPoolEntries.clear();
        categorySounds.clear();
        tickableSounds.clear();
        delayedSounds.clear();
        playingSoundsStopTime.clear();
    }
}
