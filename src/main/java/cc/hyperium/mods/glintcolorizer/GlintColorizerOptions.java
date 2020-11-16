package cc.hyperium.mods.glintcolorizer;

import cc.hyperium.Hyperium;
import cc.hyperium.config.ToggleSetting;
import cc.hyperium.config.provider.IOptionSetProvider;
import rocks.rdil.simpleconfig.Option;
import cc.hyperium.config.SliderSetting;
import java.awt.Color;

public class GlintColorizerOptions implements IOptionSetProvider {
    public static final GlintColorizerOptions INSTANCE = new GlintColorizerOptions();

    @Override
    public String getName() {
        return "Glint Colorizer";
    }

    @Option @ToggleSetting(name = "Enabled")
    public static boolean ENABLED = false;

    @Option @ToggleSetting(name = "Chroma")
    public static boolean CHROMA = true;

    @Option @SliderSetting(name = "Red", min = 0, max = 255, isInt = true)
    public static int RED = 255;

    @Option @SliderSetting(name = "Green", min = 0, max = 255, isInt = true)
    public static int GREEN = 255;

    @Option @SliderSetting(name = "Blue", min = 0, max = 255, isInt = true)
    public static int BLUE = 255;

    private static float[] onepoint8glintcolorF = Color.RGBtoHSB(RED, GREEN, BLUE, null);
    public static int onepoint8glintcolorI = Color.HSBtoRGB(onepoint8glintcolorF[0], onepoint8glintcolorF[1], onepoint8glintcolorF[2]);

    public static void setonepoint8color(int r, int g, int b) {
        RED = r;
        GREEN = g;
        BLUE = b;
        onepoint8glintcolorF = Color.RGBtoHSB(RED, GREEN, BLUE, null);
        onepoint8glintcolorI = Color.HSBtoRGB(onepoint8glintcolorF[0], onepoint8glintcolorF[1], onepoint8glintcolorF[2]);
        Hyperium.CONFIG.save();
    }

    public void setChroma(boolean bool) {
        CHROMA = bool;
        if (!CHROMA) {
            onepoint8glintcolorF = Color.RGBtoHSB(RED, GREEN, BLUE, null);
            onepoint8glintcolorI = Color.HSBtoRGB(onepoint8glintcolorF[0], onepoint8glintcolorF[1], onepoint8glintcolorF[2]);
        }
        Hyperium.CONFIG.save();
    }
}
