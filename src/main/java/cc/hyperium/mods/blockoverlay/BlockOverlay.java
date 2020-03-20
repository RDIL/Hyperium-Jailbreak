package cc.hyperium.mods.blockoverlay;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.mods.AbstractMod;

public class BlockOverlay extends AbstractMod {
    private BlockOverlaySettings settings;

    public BlockOverlay() {}

    @Override
    public AbstractMod init() {
        this.settings = new BlockOverlaySettings(Hyperium.folder);
        this.settings.load();
        EventBus.INSTANCE.register(new BlockOverlayRender(this));
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new BlockOverlayCommand(this));
        return this;
    }

    public BlockOverlaySettings getSettings() {
        return this.settings;
    }
}
