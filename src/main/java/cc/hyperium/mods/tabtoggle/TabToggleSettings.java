package cc.hyperium.mods.tabtoggle;

import static cc.hyperium.config.Category.TAB_TOGGLE;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.ToggleSetting;

public class TabToggleSettings {
    public static boolean TAB_TOGGLED = false;
    @ConfigOpt
    @ToggleSetting(name = "Tab Toggle", category = TAB_TOGGLE, mods = true)
    public static boolean ENABLED = false;
}
