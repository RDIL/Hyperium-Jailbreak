package cc.hyperium.mods.itemphysic;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.render.RenderEvent;
import cc.hyperium.mods.itemphysic.physics.ClientPhysic;

public class EventHandlerLite {
    @InvokeEvent
    public void onTick(RenderEvent e) {
        ClientPhysic.tick = System.nanoTime();
    }
}
