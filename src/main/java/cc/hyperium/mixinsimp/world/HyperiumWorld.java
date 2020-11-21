package cc.hyperium.mixinsimp.world;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.world.SpawnpointChangeEvent;
import cc.hyperium.mixins.IMixinWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.util.ReportedException;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Iterator;
import java.util.List;

public class HyperiumWorld {
    private World parent;

    public HyperiumWorld(World parent) {
        this.parent = parent;
    }

    public void setSpawnPoint(BlockPos pos) {
        EventBus.INSTANCE.post(new SpawnpointChangeEvent(pos));
    }

    public void checkLightFor(CallbackInfoReturnable<Boolean> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) ci.setReturnValue(false);
    }

    public void getLightFromNeighborsFor(EnumSkyBlock type, BlockPos pos, CallbackInfoReturnable<Integer> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) ci.setReturnValue(15);
    }

    public double getHorizon(WorldInfo worldInfo) {
        if (Settings.VOID_FLICKER_FIX) return 0.0;
        return worldInfo.getTerrainType() == WorldType.FLAT ? 0.0D : 63.0D;
    }

    public void getLightFromNeighbor(CallbackInfoReturnable<Integer> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) ci.setReturnValue(15);
    }

    public void getRawLight(CallbackInfoReturnable<Integer> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) ci.setReturnValue(15);
    }

    public void getLight(CallbackInfoReturnable<Integer> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) ci.setReturnValue(15);
    }

    public void updateEntities(Profiler theProfiler, List<Entity> weatherEffects, List<Entity> loadedEntityList, List<Entity> unloadedEntityList, List<TileEntity> tickableTileEntities, WorldBorder worldBorder, List<TileEntity> loadedTileEntityList, List<TileEntity> tileEntitiesToBeRemoved, List<TileEntity> addedTileEntityList) {
        theProfiler.startSection("entities");
        theProfiler.startSection("global");

        for (int i = 0; i < weatherEffects.size(); ++i) {
            Entity entity = weatherEffects.get(i);

            try {
                ++entity.ticksExisted;
                entity.onUpdate();
            } catch (Throwable throwable2) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable2, "Ticking entity");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being ticked");
                entity.addEntityCrashInfo(crashreportcategory);
                throw new ReportedException(crashreport);
            }

            if (entity.isDead) weatherEffects.remove(i--);
        }

        theProfiler.endStartSection("remove");
        loadedEntityList.removeAll(unloadedEntityList);

        for (Entity entity1 : unloadedEntityList) {
            int j = entity1.chunkCoordX;
            int l1 = entity1.chunkCoordZ;

            if (entity1.addedToChunk && ((IMixinWorld) parent).callIsChunkLoaded(j, l1, true)) {
                parent.getChunkFromChunkCoords(j, l1).removeEntity(entity1);
            }
        }

        for (Entity entity : unloadedEntityList) {
            ((IMixinWorld) parent).callOnEntityRemoved(entity);
        }

        unloadedEntityList.clear();
        updateDefaultEntities(theProfiler, loadedEntityList);
        theProfiler.startSection("blockEntities");
        ((IMixinWorld) parent).setProcessingLoadedTiles(true);
        Iterator<TileEntity> iterator = tickableTileEntities.iterator();

        while (iterator.hasNext()) {
            TileEntity tileentity = iterator.next();

            if (!tileentity.isInvalid() && tileentity.hasWorldObj()) {
                BlockPos blockpos = tileentity.getPos();

                if (parent.isBlockLoaded(blockpos) && worldBorder.contains(blockpos)) {
                    try {
                        ((ITickable) tileentity).update();
                    } catch (Throwable throwable) {
                        CrashReport crashreport2 = CrashReport.makeCrashReport(throwable, "Ticking block entity");
                        CrashReportCategory crashreportcategory1 = crashreport2.makeCategory("Block entity being ticked");
                        tileentity.addInfoToCrashReport(crashreportcategory1);
                        throw new ReportedException(crashreport2);
                    }
                }
            }

            if (tileentity.isInvalid()) {
                iterator.remove();
                loadedTileEntityList.remove(tileentity);

                if (parent.isBlockLoaded(tileentity.getPos())) {
                    parent.getChunkFromBlockCoords(tileentity.getPos()).removeTileEntity(tileentity.getPos());
                }
            }
        }

        ((IMixinWorld) parent).setProcessingLoadedTiles(false);

        if (!tileEntitiesToBeRemoved.isEmpty()) {
            tickableTileEntities.removeAll(tileEntitiesToBeRemoved);
            loadedTileEntityList.removeAll(tileEntitiesToBeRemoved);
            tileEntitiesToBeRemoved.clear();
        }

        theProfiler.endStartSection("pendingBlockEntities");

        if (!addedTileEntityList.isEmpty()) {
            for (TileEntity tileentity1 : addedTileEntityList) {
                if (!tileentity1.isInvalid()) {
                    if (!loadedTileEntityList.contains(tileentity1)) {
                        parent.addTileEntity(tileentity1);
                    }

                    if (parent.isBlockLoaded(tileentity1.getPos())) {
                        parent.getChunkFromBlockCoords(tileentity1.getPos()).addTileEntity(tileentity1.getPos(), tileentity1);
                    }

                    parent.markBlockForUpdate(tileentity1.getPos());
                }
            }
            addedTileEntityList.clear();
        }

        theProfiler.endSection();
        theProfiler.endSection();
    }

    private void updateDefaultEntities(Profiler theProfiler, List<Entity> loadedEntityList) {
        theProfiler.endStartSection("regular");
        for (int i1 = 0; i1 < loadedEntityList.size(); ++i1) {
            Entity entity2 = loadedEntityList.get(i1);

            if (entity2.ridingEntity != null) {
                if (!entity2.ridingEntity.isDead && entity2.ridingEntity.riddenByEntity == entity2) continue;
                entity2.ridingEntity.riddenByEntity = null;
                entity2.ridingEntity = null;
            }

            theProfiler.startSection("tick");
            updateEntity(entity2);
            theProfiler.endSection();
            theProfiler.startSection("remove");

            if (entity2.isDead) {
                int k1 = entity2.chunkCoordX;
                int i2 = entity2.chunkCoordZ;

                if (entity2.addedToChunk && ((IMixinWorld) parent).callIsChunkLoaded(k1, i2, true)) {
                    parent.getChunkFromChunkCoords(k1, i2).removeEntity(entity2);
                }

                loadedEntityList.remove(i1--);
                ((IMixinWorld) parent).callOnEntityRemoved(entity2);
            }

            theProfiler.endSection();
        }
        theProfiler.endSection();
    }

    private void updateEntity(Entity entity) {
        if (!entity.isDead) {
            try {
                parent.updateEntity(entity);
            } catch (Throwable throwable1) {
                CrashReport crashreport1 = CrashReport.makeCrashReport(throwable1, "Ticking entity");
                CrashReportCategory crashreportcategory2 = crashreport1.makeCategory("Entity being ticked");
                entity.addEntityCrashInfo(crashreportcategory2);
                throw new ReportedException(crashreport1);
            }
        }
    }
}
