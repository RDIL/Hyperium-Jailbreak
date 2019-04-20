package cc.hyperium.purchases.packages;

import cc.hyperium.purchases.AbstractHyperiumPurchase;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.utils.JsonHolder;

public class EarsCosmetic extends AbstractHyperiumPurchase {
    private final boolean enabled;

    public EarsCosmetic(EnumPurchaseType type, JsonHolder data) {
        super(type, data);
        enabled = getData().optBoolean("glintColorizer");
    }

    public boolean isEnabled() {
        return enabled;
    }
}

