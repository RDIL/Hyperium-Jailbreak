package cc.hyperium.config.provider;

import cc.hyperium.config.ToggleSetting;
import rocks.rdil.simpleconfig.Option;

public class IntegrationOptionsProvider implements IOptionSetProvider {
    public static final IntegrationOptionsProvider INSTANCE = new IntegrationOptionsProvider();

    @Override
    public String getName() {
        return "Integrations";
    }
    
    @Option @ToggleSetting(name = "Fullbright")
    public static boolean FULLBRIGHT = true;

    @Option @ToggleSetting(name = "Compact Chat")
    public static boolean COMPACT_CHAT = false;

    @Option @ToggleSetting(name = "Disable Chat Background")
    public static boolean FASTCHAT = true;

    @Option @ToggleSetting(name = "Show Dots on Name Tags")
    public static boolean SHOW_DOTS_ON_NAME_TAGS = false;

    @Option @ToggleSetting(name = "Void Flicker Fix")
    public static boolean VOID_FLICKER_FIX = true;

    @Option @ToggleSetting(name = "Disable Sounds When Tabbed Out")
    public static boolean SMART_SOUNDS = false;

    @Option @ToggleSetting(name = "Critical Particle Fix")
    public static boolean CRIT_FIX = true;

    @Option @ToggleSetting(name = "Require Holding Perspective Key")
    public static boolean PERSPECTIVE_HOLD = false;

    @Option @ToggleSetting(name = "Windowed Fullscreen")
    public static boolean WINDOWED_FULLSCREEN = false;

    @Option @ToggleSetting(name = "Static FOV")
    public static boolean STATIC_FOV = false;
}
