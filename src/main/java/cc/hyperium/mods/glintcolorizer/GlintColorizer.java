package cc.hyperium.mods.glintcolorizer;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.client.TickEvent;
import cc.hyperium.mods.AbstractMod;
import java.awt.Color;

public class GlintColorizer extends AbstractMod {
    @Override
    public AbstractMod init() {
        Hyperium.CONFIG.register(GlintColorizerOptions.INSTANCE);
        EventBus.INSTANCE.register(this);
        GlintColorizerOptions.setonepoint8color(GlintColorizerOptions.RED, GlintColorizerOptions.GREEN, GlintColorizerOptions.BLUE);
        return this;
    }

    @SuppressWarnings("unused")
    @InvokeEvent
    public void onTick(TickEvent e) {
        if (!GlintColorizerOptions.ENABLED) {
            if (GlintColorizerOptions.onepoint8glintcolorI != -8372020) {
                GlintColorizerOptions.onepoint8glintcolorI = -8372020;
            }
            return;
        }
        if (GlintColorizerOptions.CHROMA) {
            GlintColorizerOptions.onepoint8glintcolorI = Color.HSBtoRGB(System.currentTimeMillis() % 10000L / 10000.0f, 0.8f, 0.8f);
            return;
        }
        GlintColorizerOptions.onepoint8glintcolorI = getIntFromColor(GlintColorizerOptions.RED, GlintColorizerOptions.GREEN, GlintColorizerOptions.BLUE);
    }

    private static int getIntFromColor(int red, int green, int blue) {
        red = (red << 16) & 0x00FF0000;
        green = (green << 8) & 0x0000FF00;
        blue = blue & 0x000000FF;
        return 0xFF000000 | red | green | blue;
    }
}
