package cc.hyperium.purchases;

import cc.hyperium.config.Settings;
import cc.hyperium.utils.JsonHolder;

public class PurchaseSettings {
    private boolean wingsDisabled;
    private double wingsScale;
    private EnumPurchaseType currentHatType;
    private EnumPurchaseType companion;

    public PurchaseSettings(JsonHolder source) {
        this.wingsDisabled = source.optJSONObject("wings").optBoolean("disabled");
        this.wingsScale = source.optJSONObject("wings").optDouble("scale", Settings.WINGS_SCALE);
        this.currentHatType = EnumPurchaseType.parse(source.optJSONObject("hat").optString("current_type"));
    }

    public double getWingsScale() {
        return wingsScale;
    }

    public boolean isWingsDisabled() {
        return wingsDisabled;
    }

    public EnumPurchaseType getCurrentHatType() {
        return currentHatType;
    }
}
