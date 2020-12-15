package cc.hyperium.addons;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.bossbar.BossbarAddon;
import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.addons.sidebar.SidebarAddon;
import cc.hyperium.mods.HyperiumModIntegration;

/**
 * Hyperium's internal addon manager.
 *
 * !! Internal addons are now internal mods. Use {@link cc.hyperium.mods.HyperiumModIntegration} instead. !!
 *
 * @see HyperiumModIntegration
 * @see Hyperium#getModIntegration()
 */
public class InternalAddons {
    /**
     * Returns the custom crosshair addon instance.
     *
     * @return The custom crosshair addon instance.
     * @deprecated Use {@link HyperiumModIntegration#getCustomCrosshairAddon()}.
     */
    @Deprecated
    public CustomCrosshairAddon getCustomCrosshairAddon() {
        return Hyperium.INSTANCE.getModIntegration().getCustomCrosshairAddon();
    }

    /**
     * Returns the sidebar addon instance.
     *
     * @return The sidebar addon instance.
     * @deprecated Use {@link HyperiumModIntegration#getSidebarAddon()}.
     */
    @Deprecated
    public SidebarAddon getSidebarAddon() {
        return Hyperium.INSTANCE.getModIntegration().getSidebarAddon();
    }

    /**
     * Returns the bossbar addon instance.
     *
     * @return The bossbar addon instance.
     * @deprecated Use {@link HyperiumModIntegration#getBossbarAddon()}.
     */
    @Deprecated
    public BossbarAddon getBossbarAddon() {
        return Hyperium.INSTANCE.getModIntegration().getBossbarAddon();
    }
}
