package cc.hyperium.config.provider;

import cc.hyperium.config.SelectorSetting;
import cc.hyperium.config.ToggleSetting;
import cc.hyperium.styles.ButtonStyle;
import cc.hyperium.styles.GuiStyle;
import rocks.rdil.simpleconfig.Option;

public class MenusOptionsProvider implements IOptionSetProvider {
    public static final MenusOptionsProvider INSTANCE = new MenusOptionsProvider();

    @Override
    public String getName() {
        return "Menus";
    }

    @Option @SelectorSetting(name = "Main Menu Style", items = {"HYPERIUM", "DEFAULT"})
    public static String MENU_STYLE = GuiStyle.DEFAULT.name();

    @Option @SelectorSetting(name = "Pause Menu Style", items = {"HYPERIUM", "DEFAULT"})
    public static String PAUSE_STYLE = GuiStyle.HYPERIUM.name();

    @Option @SelectorSetting(name = "Button Style", items = {"HYPERIUM", "HYPERIUM 2"})
    public static String BUTTON_STYLE = ButtonStyle.HYPERIUM.name();

    @Option @ToggleSetting(name = "Chroma Buttons (For Hyperium 2 Style)")
    public static boolean H2_BUTTONS_CHROMA = false;
}
