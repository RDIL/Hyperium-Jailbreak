package cc.hyperium.mods.tabtoggle;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.mods.AbstractMod;

public class TabToggleMod extends AbstractMod {
    @Override
    public AbstractMod init() {
        EventBus.INSTANCE.register(new TabToggleEventListener());

        TabToggleSettings object = new TabToggleSettings();
        Hyperium.CONFIG.register(object);
        return this;
    }
}
