package cc.hyperium.event.render;

import cc.hyperium.event.Event;

/**
 * Called when the HUD is rendered.
 */
public class RenderHUDEvent extends Event {
    private final float partialTicks;

    public RenderHUDEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}
