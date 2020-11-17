package cc.hyperium.config.provider;

import cc.hyperium.config.SelectorSetting;
import cc.hyperium.config.ToggleSetting;
import rocks.rdil.simpleconfig.Option;

public class FortniteCompassOptionsProvider implements IOptionSetProvider {
    public static final FortniteCompassOptionsProvider INSTANCE = new FortniteCompassOptionsProvider();

    @Override
    public String getName() {
        return "Fortnite Compass";
    }

    @Option @ToggleSetting(name = "Enable Fortnite Compass")
    public static boolean FNCOMPASS_ENABLED = false;

    @Option @ToggleSetting(name = "Show Background")
    public static boolean FNCOMPASS_BACKGROUND = true;

    @Option @ToggleSetting(name = "Chroma")
    public static boolean FNCOMPASS_CHROMA = false;

    @Option @ToggleSetting(name = "Text Shadow")
    public static boolean FNCOMPASS_SHADOW = true;

    @Option @SelectorSetting(name = "Detail Level", items = {"0", "1", "2"})
    public static String FNCOMPASS_DETAILS = "2";
}
