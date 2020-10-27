package cc.hyperium.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.world.ChunkLoadEvent;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.world.chunk.Chunk;

@Mixin(ChunkProviderClient.class)
public class MixinChunkProviderClient {
    @Inject(method = "loadChunk", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    public void loadChunk(Chunk chunk, CallbackInfo ci) {
        EventBus.INSTANCE.post(new ChunkLoadEvent(chunk));
    }
}
