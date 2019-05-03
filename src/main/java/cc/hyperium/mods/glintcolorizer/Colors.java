package cc.hyperium.mods.glintcolorizer;

import java.awt.Color;

public class Colors {
    private static float[] onepoint8glintcolorF = Color.RGBtoHSB(Settings.glintR, Settings.glintG, Settings.glintB, null);
    public static int onepoint8glintcolorI = Color.HSBtoRGB(onepoint8glintcolorF[0], onepoint8glintcolorF[1], onepoint8glintcolorF[2]);

    public static void setonepoint8color(int r, int g, int b) {
        glintR = r;
        glintG = g;
        glintB = b;
        onepoint8glintcolorF = Color.RGBtoHSB(Settings.glintR, Settings.glintG, Settings.glintB, null);
        onepoint8glintcolorI = Color.HSBtoRGB(onepoint8glintcolorF[0], onepoint8glintcolorF[1], onepoint8glintcolorF[2]);
    }

    public void setChroma(boolean bool) {
        if (!(glintcolorChroma = bool)) {
            onepoint8glintcolorF = Color.RGBtoHSB(Settings.glintR, Settings.glintG, Settings.glintB, null);
            onepoint8glintcolorI = Color.HSBtoRGB(onepoint8glintcolorF[0], onepoint8glintcolorF[1], onepoint8glintcolorF[2]);
        }
    }

    private int getIntFromColor(int red, int green, int blue) {
        red = (red << 16) & 0x00FF0000;
        green = (green << 8) & 0x0000FF00;
        blue = blue & 0x000000FF;
        return 0xFF000000 | red | green | blue;
    }
}
