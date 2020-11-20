package cc.hyperium.mods.tabtoggle;

import cc.hyperium.config.ToggleSetting;
import cc.hyperium.config.provider.IOptionSetProvider;
import rocks.rdil.simpleconfig.Option;

public class TabToggleSettings implements IOptionSetProvider {
    public static final TabToggleSettings INSTANCE = new TabToggleSettings();

    @Override
    public String getName() {
        return "Tab Toggle";
    }

    public static boolean TAB_TOGGLED = false;

    @Option @ToggleSetting(name = "Tab Toggle")
    public static boolean ENABLED = false;
}
