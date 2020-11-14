package cc.hyperium.event.world;

import net.minecraft.client.renderer.RenderGlobal;

public class RenderWorldFinalPassEvent {
    private final float partialTicks;

    public RenderWorldFinalPassEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
