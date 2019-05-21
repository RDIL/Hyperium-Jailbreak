package cc.hyperium.addons;
import cc.hyperium.config.Settings;
import cc.hyperium.addons.bossbar.BossbarAddon;
import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.addons.sidebar.SidebarAddon;

public class InternalAddons {
    private static final CustomCrosshairAddon customCrosshairAddon;
    private static final SidebarAddon sidebarAddon;
    private static final BossbarAddon bossbarAddon;

    public InternalAddons() {
        this.customCrosshairAddon = (CustomCrosshairAddon) new CustomCrosshairAddon();
        this.sidebarAddon = (SidebarAddon) new SidebarAddon();
        this.bossbarAddon = (BossbarAddon) new BossbarAddon();
        if (!Settings.FPS) {
            this.customCrosshairAddon.init();
        }
        this.sidebarAddon.init();
        this.bossbarAddon.init();
    }

    public static CustomCrosshairAddon getCustomCrosshairAddon() {
        return customCrosshairAddon;
    }

    public static SidebarAddon getSidebarAddon() {
        return sidebarAddon;
    }

    public static BossbarAddon getBossbarAddon() {
        return bossbarAddon;
    }
}
