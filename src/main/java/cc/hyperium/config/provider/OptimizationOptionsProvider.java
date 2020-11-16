package cc.hyperium.config.provider;

import cc.hyperium.config.SelectorSetting;
import cc.hyperium.config.ToggleSetting;
import rocks.rdil.simpleconfig.Option;

public class OptimizationOptionsProvider implements IOptionSetProvider {
    public static final OptimizationOptionsProvider INSTANCE = new OptimizationOptionsProvider();

	@Override
	public String getName() {
		return "Optimizations";
	}
    
    @Option @ToggleSetting(name = "FPS Mode (activates after restart)")
    public static boolean FPS = false;

    @Option @SelectorSetting(name = "Max World Particles", items = {"1000", "2000", "4000", "6000", "8000", "10000", "20000", "50000"})
    public static String MAX_WORLD_PARTICLES_STRING = "10000";

    @Option @ToggleSetting(name = "Disable Enchant Glint")
    public static boolean DISABLE_ENCHANT_GLINT = false;

    @Option @ToggleSetting(name = "Disable Titles")
    public static boolean HIDE_TITLES = false;

    @Option @ToggleSetting(name = "Disable Lightning")
    public static boolean DISABLE_LIGHTNING = false;

    @Option @ToggleSetting(name = "Disable Item Frames")
    public static boolean DISABLE_ITEMFRAMES = false;

    @Option @ToggleSetting(name = "Multi-CPU Particle Rendering (Requires 4+ CPU Cores)")
    public static boolean MULTI_CPU_PARTICLE_RENDERING = false;
}
