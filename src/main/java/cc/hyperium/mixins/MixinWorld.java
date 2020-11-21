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
import cc.hyperium.event.entity.EntityJoinWorldEvent;
import cc.hyperium.mixinsimp.world.HyperiumWorld;
import net.minecraft.entity.Entity;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;

@Mixin(World.class)
public abstract class MixinWorld {
    @Shadow @Final public List<Entity> loadedEntityList;
    @Shadow @Final public Profiler theProfiler;
    @Shadow @Final public List<Entity> weatherEffects;
    @Shadow @Final public List<TileEntity> tickableTileEntities;
    @Shadow @Final public List<TileEntity> loadedTileEntityList;
    @Shadow @Final protected List<Entity> unloadedEntityList;
    @Shadow protected WorldInfo worldInfo;
    @Shadow @Final private WorldBorder worldBorder;
    @Shadow @Final private List<TileEntity> tileEntitiesToBeRemoved;
    @Shadow @Final private List<TileEntity> addedTileEntityList;
    private HyperiumWorld hyperiumWorld = new HyperiumWorld((World) (Object) this);

    @Inject(method = "setSpawnPoint", at = @At("HEAD"))
    private void setSpawnPoint(BlockPos pos, CallbackInfo ci) {
        hyperiumWorld.setSpawnPoint(pos);
    }

    @Inject(method = "checkLightFor", at = @At("HEAD"), cancellable = true)
    private void checkLightFor(EnumSkyBlock lightType, BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
        hyperiumWorld.checkLightFor(ci);
    }

    @Inject(method = "getLightFromNeighborsFor", at = @At("HEAD"), cancellable = true)
    private void getLightFromNeighborsFor(EnumSkyBlock type, BlockPos pos, CallbackInfoReturnable<Integer> ci) {
        hyperiumWorld.getLightFromNeighborsFor(type, pos, ci);
    }

    /**
     * @author hyperium
     */
    @Overwrite
    public double getHorizon() {
        return hyperiumWorld.getHorizon(worldInfo);
    }

    @Inject(method = "getLightFromNeighbors", at = @At("HEAD"), cancellable = true)
    private void getLightFromNeighbor(BlockPos pos, CallbackInfoReturnable<Integer> ci) {
        hyperiumWorld.getLightFromNeighbor(ci);
    }

    @Inject(method = "getRawLight", at = @At("HEAD"), cancellable = true)
    private void getRawLight(BlockPos pos, EnumSkyBlock lightType, CallbackInfoReturnable<Integer> ci) {
        hyperiumWorld.getRawLight(ci);
    }

    @Inject(method = "getLight(Lnet/minecraft/util/BlockPos;)I", at = @At("HEAD"), cancellable = true)
    private void getLight(BlockPos pos, CallbackInfoReturnable<Integer> ci) {
        hyperiumWorld.getLight(ci);
    }

    @Inject(method = "getLight(Lnet/minecraft/util/BlockPos;Z)I", at = @At("HEAD"), cancellable = true)
    private void getLight(BlockPos pos, boolean checkNeighbors, CallbackInfoReturnable<Integer> ci) {
        hyperiumWorld.getLight(ci);
    }

    /**
     * @author hyperium
     */
    @Overwrite
    public void updateEntities() {
        hyperiumWorld.updateEntities(theProfiler, weatherEffects, loadedEntityList, unloadedEntityList, tickableTileEntities, worldBorder, loadedTileEntityList, tileEntitiesToBeRemoved, addedTileEntityList);
    }

    @Inject(method = "spawnEntityInWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;onEntityAdded(Lnet/minecraft/entity/Entity;)V"))
    public void spawnEntityInWorld(Entity entityIn, CallbackInfoReturnable<Entity> ci) {
        EventBus.INSTANCE.post(new EntityJoinWorldEvent(entityIn, (World) (Object) this));
    }
}
