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

import cc.hyperium.event.EventBus;
import cc.hyperium.event.entity.EntityEnterChunkEvent;
import cc.hyperium.event.world.ChunkLoadEvent;
import cc.hyperium.mixinsimp.world.HyperiumChunk;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Chunk.class)
public class MixinChunk {
    @Inject(method = "getLightFor", at = @At("HEAD"), cancellable = true)
    private void getLightFor(EnumSkyBlock type, BlockPos pos, CallbackInfoReturnable<Integer> ci) {
        HyperiumChunk.getLightFor(ci);
    }

    @Inject(method = "getLightSubtracted", at = @At("HEAD"), cancellable = true)
    private void getLightSubtracted(BlockPos pos, int amount, CallbackInfoReturnable<Integer> ci) {
        HyperiumChunk.getLightSubtracted(ci);
    }

    @Inject(method = "addEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/MathHelper;floor_double(D)I", ordinal = 2))
    public void addEntity(Entity entityIn, CallbackInfo ci) {
        EventBus.INSTANCE.post(new EntityEnterChunkEvent(entityIn, (Chunk) (Object) this));
    }

    @Inject(method = "onChunkLoad", at = @At("TAIL"))
    public void loadChunk(CallbackInfo ci) {
        EventBus.INSTANCE.post(new ChunkLoadEvent((Chunk) (Object) this));
    }
}
