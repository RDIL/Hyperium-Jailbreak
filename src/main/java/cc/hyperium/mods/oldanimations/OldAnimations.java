package cc.hyperium.mods.oldanimations;

import cc.hyperium.event.EventBus;
import cc.hyperium.mods.AbstractMod;

public class OldAnimations extends AbstractMod {
    public OldAnimations() {}

    @Override
    public AbstractMod init() {
        EventBus.INSTANCE.register(new AnimationEventHandler());
        return this;
    }
}
