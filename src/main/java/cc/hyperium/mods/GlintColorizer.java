package cc.hyperium.mods;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import java.awt.Color;

public class GlintColorizer extends AbstractMod {
    private static float[] onepoint8glintcolorF = Color.RGBtoHSB(Settings.glintR, Settings.glintG, Settings.glintB, null);
    public static int onepoint8glintcolorI = Color.HSBtoRGB(onepoint8glintcolorF[0], onepoint8glintcolorF[1], onepoint8glintcolorF[2]);

    @Override
    public AbstractMod init() {
        EventBus.INSTANCE.register(this);
        setonepoint8color(Settings.glintR, Settings.glintG, Settings.glintB);
        return this;
    }

    @SuppressWarnings("unused")
    @InvokeEvent
    public void onTick(TickEvent e) {
        if (!Settings.glintColorizer) {
            if (onepoint8glintcolorI != -8372020) onepoint8glintcolorI = -8372020;
            return;
        }
        if (Settings.glintcolorChroma) {
            onepoint8glintcolorI = Color.HSBtoRGB(System.currentTimeMillis() % 10000L / 10000.0f, 0.8f, 0.8f);
            return;
        }
        onepoint8glintcolorI = getIntFromColor(Settings.glintR, Settings.glintG, Settings.glintB);
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Glint Colorizer", "1.0", "powns");
    }

    private int getIntFromColor(int red, int green, int blue) {
        red = (red << 16) & 0x00FF0000;
        green = (green << 8) & 0x0000FF00;
        blue = blue & 0x000000FF;
        return 0xFF000000 | red | green | blue;
    }

    public static void setonepoint8color(int r, int g, int b) {
        Settings.glintR = r;
        Settings.glintG = g;
        Settings.glintB = b;
        onepoint8glintcolorF = Color.RGBtoHSB(Settings.glintR, Settings.glintG, Settings.glintB, null);
        onepoint8glintcolorI = Color.HSBtoRGB(onepoint8glintcolorF[0], onepoint8glintcolorF[1], onepoint8glintcolorF[2]);
    }

    public void setChroma(boolean bool) {
        if (!(Settings.glintcolorChroma = bool)) {
            onepoint8glintcolorF = Color.RGBtoHSB(Settings.glintR, Settings.glintG, Settings.glintB, null);
            onepoint8glintcolorI = Color.HSBtoRGB(onepoint8glintcolorF[0], onepoint8glintcolorF[1], onepoint8glintcolorF[2]);
        }
    }
}
