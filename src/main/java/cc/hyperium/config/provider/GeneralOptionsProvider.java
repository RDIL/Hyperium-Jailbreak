package cc.hyperium.config.provider;

import cc.hyperium.config.ToggleSetting;
import rocks.rdil.simpleconfig.Option;

public class GeneralOptionsProvider implements IOptionSetProvider {
    public static final GeneralOptionsProvider INSTANCE = new GeneralOptionsProvider();

    @Override
    public String getName() {
        return "General";
    }

    @Option @ToggleSetting(name = "Roman Numerals")
    public static boolean ROMAN_NUMERALS = true;

    @Option @ToggleSetting(name = "Show Own Name Tag")
    public static boolean SHOW_OWN_NAME = false;

    @Option @ToggleSetting(name = "Transparent GUI Backgrounds")
    public static boolean FAST_CONTAINER = false;

    @Option
    public static double HEAD_SCALE_FACTOR = 1.0;

    @Option @ToggleSetting(name = "Online Indicators")
    public static boolean SHOW_ONLINE_PLAYERS = true;

    @Option @ToggleSetting(name = "Sprint Bypass Static FOV")
    public static boolean staticFovSprintModifier;

    @Option @ToggleSetting(name = "Show Sprint/Perspective Messages")
    public static boolean SPRINT_PERSPECTIVE_MESSAGES = true;

    @Option public static int MAX_WORLD_PARTICLES_INT = 10000;

    @Option @ToggleSetting(name = "Hide Name Tags in F1")
    public static boolean BETTERF1 = false;

    @Option @ToggleSetting(name = "Blink at the End of Night Vision")
    public static boolean NIGHT_VISION_BLINKING = false;
}
