package cc.hyperium.mods.glintcolorizer;

import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.ToggleSetting;
import cc.hyperium.config.SliderSetting;
import static cc.hyperium.config.Category.GLINTCOLORIZER;
import java.awt.Color;

public class Colors {
    @ConfigOpt
    @ToggleSetting(name = "Chroma", category = GLINTCOLORIZER, mods = true)
    public static boolean glintcolorChroma;

    @ConfigOpt
    @SliderSetting(name = "Red", mods = true, category = GLINTCOLORIZER, min = 0, max = 255, isInt = true)
    public static int glintR = 255;

    @ConfigOpt
    @SliderSetting(name = "Green", mods = true, category = GLINTCOLORIZER, min = 0, max = 255, isInt = true)
    public static int glintG = 255;

    @ConfigOpt
    @SliderSetting(name = "Blue", mods = true, category = GLINTCOLORIZER, min = 0, max = 255, isInt = true)
    public static int glintB = 255;

    @ConfigOpt
    @ToggleSetting(category = GLINTCOLORIZER, mods = true, name = "Enabled")
    public static boolean glintColorizer = false;

    private static float[] onepoint8glintcolorF = Color.RGBtoHSB(glintR, glintG, glintB, null);
    public static int onepoint8glintcolorI = Color.HSBtoRGB(onepoint8glintcolorF[0], onepoint8glintcolorF[1], onepoint8glintcolorF[2]);

    public static void setonepoint8color(int r, int g, int b) {
        glintR = r;
        glintG = g;
        glintB = b;
        onepoint8glintcolorF = Color.RGBtoHSB(glintR, glintG, glintB, null);
        onepoint8glintcolorI = Color.HSBtoRGB(onepoint8glintcolorF[0], onepoint8glintcolorF[1], onepoint8glintcolorF[2]);
    }

    public void setChroma(boolean bool) {
        if (!(glintcolorChroma = bool)) {
            onepoint8glintcolorF = Color.RGBtoHSB(glintR, glintG, glintB, null);
            onepoint8glintcolorI = Color.HSBtoRGB(onepoint8glintcolorF[0], onepoint8glintcolorF[1], onepoint8glintcolorF[2]);
        }
    }
}
