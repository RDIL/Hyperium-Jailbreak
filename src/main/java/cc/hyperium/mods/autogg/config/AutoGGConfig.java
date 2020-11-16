package cc.hyperium.mods.autogg.config;

import cc.hyperium.config.SliderSetting;
import cc.hyperium.config.ToggleSetting;
import cc.hyperium.config.provider.IOptionSetProvider;
import rocks.rdil.simpleconfig.Option;

public class AutoGGConfig implements IOptionSetProvider {
    public static final AutoGGConfig INSTANCE = new AutoGGConfig();

    @Override
    public String getName() {
        return "Auto GG";
    }

    @Option @ToggleSetting(name = "Hide GG's at end of game")
    public static boolean ANTI_GG = false;

    @Option @SliderSetting(name = "Delay", min = 0, max = 5, isInt = true)
    public static int DELAY = 1;

    @Option @ToggleSetting(name = "Enabled")
    public static boolean ENABLED = true;

    @Option @ToggleSetting(name = "Say Good Game instead of GG")
    public static boolean SAY_GOOD_GAME_NOT_GG = false;

    @Option @ToggleSetting(name = "Say in Lowercase")
    public static boolean LOWERCASE = false;

    public static int getDelay() {
        return DELAY < 0 ? 1 : DELAY > 5 ? 1 : DELAY;
    }
}
