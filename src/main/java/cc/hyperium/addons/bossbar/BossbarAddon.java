package cc.hyperium.addons.bossbar;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.AbstractAddon;
import cc.hyperium.addons.bossbar.commands.CommandBossbar;
import cc.hyperium.addons.bossbar.config.BossbarConfig;
import cc.hyperium.addons.bossbar.gui.GuiBossbarSetting;

public class BossbarAddon extends AbstractAddon {
    private BossbarConfig bossbarConfig;
    private GuiBossbarSetting guiBossBarSetting;

    public GuiBossbarSetting getGuiBossBarSetting() {
        return guiBossBarSetting;
    }

    @Override
    public AbstractAddon init() {
        bossbarConfig = new BossbarConfig();
        guiBossBarSetting = new GuiBossbarSetting(this);
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandBossbar(this));
        return this;
    }

    public BossbarConfig getConfig() {
        return bossbarConfig;
    }

    @Override
    public Metadata getAddonMetadata() {
        return new AbstractAddon.Metadata(this, "BossbarAddon", "1", "SiroQ");
    }
}
