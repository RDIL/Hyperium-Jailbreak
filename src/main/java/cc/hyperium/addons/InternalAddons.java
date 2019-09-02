package cc.hyperium.addons;
import cc.hyperium.config.Settings;
import cc.hyperium.addons.bossbar.BossbarAddon;
import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.addons.sidebar.SidebarAddon;

public class InternalAddons {
    private final CustomCrosshairAddon customCrosshairAddon;
    private final SidebarAddon sidebarAddon;
    private final BossbarAddon bossbarAddon;

    public InternalAddons() {
        this.customCrosshairAddon = new CustomCrosshairAddon();
        this.sidebarAddon = new SidebarAddon();
        this.bossbarAddon = new BossbarAddon();
        if (!Settings.FPS) {
            this.customCrosshairAddon.init();
        }
        this.sidebarAddon.init();
        this.bossbarAddon.init();
    }

    public CustomCrosshairAddon getCustomCrosshairAddon() {
        return customCrosshairAddon;
    }

    public SidebarAddon getSidebarAddon() {
        return sidebarAddon;
    }

    public BossbarAddon getBossbarAddon() {
        return bossbarAddon;
    }
}
