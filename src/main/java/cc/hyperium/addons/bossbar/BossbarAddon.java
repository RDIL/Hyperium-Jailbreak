package cc.hyperium.addons.bossbar;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.bossbar.commands.CommandBossbar;
import cc.hyperium.addons.bossbar.config.BossbarConfig;
import cc.hyperium.addons.bossbar.gui.GuiBossbarSetting;
import cc.hyperium.mods.AbstractMod;

public class BossbarAddon extends AbstractMod {
    private BossbarConfig bossbarConfig;
    private GuiBossbarSetting guiBossBarSetting;

    public GuiBossbarSetting getGuiBossBarSetting() {
        return guiBossBarSetting;
    }

    @Override
    public AbstractMod init() {
        bossbarConfig = new BossbarConfig();
        guiBossBarSetting = new GuiBossbarSetting(this);
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandBossbar(this));
        return this;
    }

    public BossbarConfig getConfig() {
        return bossbarConfig;
    }
}
