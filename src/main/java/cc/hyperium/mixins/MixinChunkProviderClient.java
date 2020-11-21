package cc.hyperium.mixins;

import net.minecraft.util.LongHashMap;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.world.ChunkLoadEvent;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ChunkProviderClient.class)
public class MixinChunkProviderClient {
    @Shadow private World worldObj;
    @Shadow private LongHashMap<Chunk> chunkMapping;
    @Shadow private List<Chunk> chunkListing;

    /**
     * @author Reece Dunham and Mojang
     */
    @Overwrite
    public Chunk loadChunk(int p_73158_1_, int p_73158_2_) {
        Chunk chunk = new Chunk(this.worldObj, p_73158_1_, p_73158_2_);
        this.chunkMapping.add(ChunkCoordIntPair.chunkXZ2Int(p_73158_1_, p_73158_2_), chunk);
        this.chunkListing.add(chunk);
        EventBus.INSTANCE.post(new ChunkLoadEvent(chunk));
        chunk.setChunkLoaded(true);
        return chunk;
    }
}
