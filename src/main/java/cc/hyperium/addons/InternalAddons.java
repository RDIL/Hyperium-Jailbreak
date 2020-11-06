package cc.hyperium.addons;

import cc.hyperium.mods.bossbar.BossbarAddon;
import cc.hyperium.mods.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.mods.sidebar.SidebarAddon;

/**
 * Container for internal addon instances.
 */
public class InternalAddons {
    private final CustomCrosshairAddon customCrosshairAddon;
    private final SidebarAddon sidebarAddon;
    private final BossbarAddon bossbarAddon;

    public InternalAddons() {
        this.customCrosshairAddon = new CustomCrosshairAddon();
        this.sidebarAddon = new SidebarAddon();
        this.bossbarAddon = new BossbarAddon();
        this.customCrosshairAddon.init();
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
