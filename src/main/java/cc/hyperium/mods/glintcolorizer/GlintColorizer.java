package cc.hyperium.mods.glintcolorizer;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.client.TickEvent;
import cc.hyperium.mods.AbstractMod;
import java.awt.Color;

public class GlintColorizer extends AbstractMod {
    private final Colors colors = new Colors();

    @Override
    public AbstractMod init() {
        Hyperium.CONFIG.register(colors);
        EventBus.INSTANCE.register(this);
        Colors.setonepoint8color(Colors.glintR, Colors.glintG, Colors.glintB);
        return this;
    }

    @SuppressWarnings("unused")
    @InvokeEvent
    public void onTick(TickEvent e) {
        if (!Colors.glintColorizer) {
            if (Colors.onepoint8glintcolorI != -8372020) Colors.onepoint8glintcolorI = -8372020;
            return;
        }
        if (Colors.glintcolorChroma) {
            Colors.onepoint8glintcolorI = Color.HSBtoRGB(System.currentTimeMillis() % 10000L / 10000.0f, 0.8f, 0.8f);
            return;
        }
        Colors.onepoint8glintcolorI = getIntFromColor(Colors.glintR, Colors.glintG, Colors.glintB);
    }

    private int getIntFromColor(int red, int green, int blue) {
        red = (red << 16) & 0x00FF0000;
        green = (green << 8) & 0x0000FF00;
        blue = blue & 0x000000FF;
        return 0xFF000000 | red | green | blue;
    }
}
