package cc.hyperium.event.render;
import cc.hyperium.event.Event;

public final class RenderEntitiesEvent extends Event {
    private final float partialTicks;
    public RenderEntitiesEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }
    public final float getPartialTicks() {
        return this.partialTicks;
    }
}
