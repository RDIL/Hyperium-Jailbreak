package cc.hyperium.mixins;

import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.world.ChunkLoadEvent;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChunkProviderClient.class)
public class MixinChunkProviderClient {
    // easy way to capture the chunk instance
    @Redirect(method = "loadChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;setChunkLoaded(Z)V"))
    public void loadChunk(Chunk chunk, boolean loaded) {
        EventBus.INSTANCE.post(new ChunkLoadEvent(chunk));
        chunk.setChunkLoaded(loaded);
    }
}
