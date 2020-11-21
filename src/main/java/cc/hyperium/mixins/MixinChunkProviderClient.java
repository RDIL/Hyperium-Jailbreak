package cc.hyperium.mixins;

import org.spongepowered.asm.mixin.Mixin;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.world.ChunkLoadEvent;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ChunkProviderClient.class)
public class MixinChunkProviderClient {
    @ModifyArg(method = "loadChunk", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), index = 1)
    public Chunk chunkListing$add(Chunk chunk) {
        EventBus.INSTANCE.post(new ChunkLoadEvent(chunk));
        return chunk;
    }
}
