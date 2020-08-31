package cc.hyperium.gui;

import cc.hyperium.config.ConfigOpt;
import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.OverlayLabel;
import cc.hyperium.gui.main.components.OverlaySlider;
import cc.hyperium.mods.glintcolorizer.Colors;

import java.lang.reflect.Field;

public class ColourOptions extends HyperiumOverlay {
    @ConfigOpt
    public static int accent_r = 136;
    @ConfigOpt
    public static int accent_g = 255;
    @ConfigOpt
    public static int accent_b = 0;

    public ColourOptions() {
        super("GUI Options", false);
        reload();
    }

    private void addSlider(String label, Field f) {
        f.setAccessible(true);
        try {
            this.getComponents().add(new OverlaySlider(label, 0, 255, ((Integer) f.get(null)).floatValue(), (i) -> {
                try {
                    f.set(null, i);
                    Colors.setonepoint8color(Colors.glintR, Colors.glintG, Colors.glintB);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }, true));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void reload() {
        try {
            this.getComponents().add(new OverlayLabel("Accent Colour:", true, () -> {}));
            addSlider("Red", this.getClass().getField("accent_r"));
            addSlider("Green", this.getClass().getField("accent_g"));
            addSlider("Ble", this.getClass().getField("accent_b"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}

