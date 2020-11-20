package cc.hyperium.config.provider;

import cc.hyperium.config.SelectorSetting;
import cc.hyperium.config.SliderSetting;
import cc.hyperium.config.ToggleSetting;
import rocks.rdil.simpleconfig.Option;

public class CosmeticOptionsProvider implements IOptionSetProvider {
    public static final CosmeticOptionsProvider INSTANCE = new CosmeticOptionsProvider();

    @Override
    public String getName() {
        return "Cosmetics";
    }
    
    @Option @SelectorSetting(name = "Hat Type", items = {"NONE", "TOPHAT", "FEZ", "LEGO"})
    public static String HAT_TYPE = "NONE";

    @Option @SelectorSetting(name = "Particle Cosmetic Mode", items = {"OFF", "PLAIN 1", "PLAIN 2", "CHROMA 1", "CHROMA 2"})
    public static String PARTICLE_MODE = "OFF";

    @Option public static int MAX_PARTICLES = 200;

    @Option @SelectorSetting(name = "Maximum Particles", items = {"200"})
    public static String MAX_PARTICLE_STRING = "200";

    @Option @ToggleSetting(name = "Show Wings")
    public static boolean ENABLE_WINGS = false;

    @Option @SliderSetting(name = "Wings Height", min = -40, max = 40)
    public static double WINGS_OFFSET = 0D;

    @Option @SliderSetting(name = "Wings Scale", min = 50, max = 200)
    public static double WINGS_SCALE = 100D;
}
