package cc.hyperium.addons;
import cc.hyperium.addons.bossbar.BossbarAddon;
import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.addons.sidebar.SidebarAddon;

public class InternalAddons {
    public InternalAddons() {
        new CustomCrosshairAddon().init();
        new SidebarAddon().init();
        new BossbarAddon().init();
    }
}
