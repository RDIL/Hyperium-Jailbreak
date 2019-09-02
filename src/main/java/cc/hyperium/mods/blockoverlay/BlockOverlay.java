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
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "BlockOverlay", "1", "aycy & powns");
    }

    public BlockOverlaySettings getSettings() {
        return this.settings;
    }
}
