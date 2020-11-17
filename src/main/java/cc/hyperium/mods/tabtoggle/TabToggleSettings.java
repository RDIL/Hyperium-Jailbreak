package cc.hyperium.mods.tabtoggle;

import cc.hyperium.config.ToggleSetting;
import rocks.rdil.simpleconfig.Option;

public class TabToggleSettings {
    public static boolean TAB_TOGGLED = false;

    @Option @ToggleSetting(name = "Tab Toggle")
    public static boolean ENABLED = false;
}
