package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.Hyperium;
import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import cc.hyperium.config.Settings;

import java.text.NumberFormat;
import java.util.Locale;

public class RatingDisplay extends DisplayItem {
    private static final NumberFormat format = NumberFormat.getNumberInstance(Locale.US);
    public final boolean delta;

    public RatingDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
        this.height = 10;
        delta = data.optBoolean("delta");
    }

    @Override
    public void draw(int x, double y, boolean config) {
        String string = "";
        if (!Settings.CHROMAHUD_SQUAREBRACE_PREFIX_OPTION) {
            string = "Rating: " + format.format(Hyperium.INSTANCE.getHandlers().getValueHandler().getRankedRating());
        } else {
            string = "[Rating] " + format.format(Hyperium.INSTANCE.getHandlers().getValueHandler().getRankedRating());
        }
        if (delta) {
            string += " (" + Hyperium.INSTANCE.getHandlers().getValueHandler().getDeltaRankedRating() + ")";
        }

        ElementRenderer.draw(x, y, string);
        this.width = config ? Minecraft.getMinecraft().fontRendererObj.getStringWidth(string) : 0;
    }
}
