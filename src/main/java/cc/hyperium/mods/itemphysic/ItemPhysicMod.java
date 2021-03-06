package cc.hyperium.mods.itemphysic;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.mods.AbstractMod;

public class ItemPhysicMod extends AbstractMod {
    @Override
    public AbstractMod init() {
        EventBus.INSTANCE.register(new EventHandlerLite());
        Hyperium.CONFIG.register(new ItemDummyContainer());

        return this;
    }
}
