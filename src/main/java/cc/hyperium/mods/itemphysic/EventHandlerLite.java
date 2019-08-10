package cc.hyperium.mods.itemphysic;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderEvent;
import cc.hyperium.mods.itemphysic.physics.ClientPhysic;
import cc.hyperium.mixinsimp.entity.HyperiumEntityRenderer;

public class EventHandlerLite {
    @InvokeEvent
    public void onTick(RenderEvent e) {
        ClientPhysic.tick = System.nanoTime();
        HyperiumEntityRenderer.INSTANCE.disableBlurShader();
    }
}
