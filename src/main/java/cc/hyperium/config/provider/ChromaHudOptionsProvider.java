package cc.hyperium.config.provider;

import cc.hyperium.config.ToggleSetting;
import rocks.rdil.simpleconfig.Option;

public class ChromaHudOptionsProvider implements IOptionSetProvider {
    public static final ChromaHudOptionsProvider INSTANCE = new ChromaHudOptionsProvider();

    @Override
    public String getName() {
        return "ChromaHUD";
    }

    @Option @ToggleSetting(name = "Show ChromaHUD")
    public static boolean SHOW_CHROMAHUD = true;

    @Option @ToggleSetting(name = "Enable Square Brace Prefix")
    public static boolean CHROMAHUD_SQUAREBRACE_PREFIX_OPTION = false;

    @Option @ToggleSetting(name = "Short Direction HUD")
    public static boolean SHORT_DIRECTION_HUD = false;
}
