/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.purchases;

import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HyperiumPurchase {
    private final UUID playerUUID;
    private final List<AbstractHyperiumPurchase> purchases = new ArrayList<>();
    private final JsonHolder response;
    private JsonHolder purchaseSettings = new JsonHolder();
    private boolean everything = false;
    private PurchaseSettings cachedSettings = new PurchaseSettings(new JsonHolder());

    public HyperiumPurchase(UUID playerUUID, JsonHolder response) {
        this.playerUUID = playerUUID;
        this.response = response;
        if (response.optBoolean("non_player"))
            return;
        everything = (response.optLong("everything") > System.currentTimeMillis());

        purchaseSettings = PurchaseApi.getInstance().get("https://api.hyperium.cc/purchaseSettings/" + (playerUUID.toString()));
        cachedSettings = new PurchaseSettings(getPurchaseSettings());
        for (JsonElement nicePackages : response.optJSONArray("hyperium")) {
            String asString = nicePackages.getAsString();
            EnumPurchaseType parse = EnumPurchaseType.parse(asString);
            if (parse != EnumPurchaseType.UNKNOWN)
                try {
                    AbstractHyperiumPurchase parse1 = PurchaseApi.getInstance().parse(parse, purchaseSettings.optJSONObject(parse.name().toLowerCase()));
                    if (parse1 != null)
                        this.purchases.add(parse1);
                } catch (Exception ignored) {}
        }
        if (everything) {
            for (EnumPurchaseType enumPurchaseType : EnumPurchaseType.values()) {
                if (getPurchase(enumPurchaseType) == null && enumPurchaseType != EnumPurchaseType.UNKNOWN) {
                    AbstractHyperiumPurchase parse1 = PurchaseApi.getInstance().parse(enumPurchaseType, purchaseSettings.optJSONObject(enumPurchaseType.name().toLowerCase()));
                    if (parse1 != null)
                        this.purchases.add(parse1);
                }
            }
        }
    }

    public PurchaseSettings getCachedSettings() {
        return cachedSettings;
    }

    public boolean isEverything() {
        return everything;
    }

    public JsonHolder getPurchaseSettings() {
        return purchaseSettings;
    }

    public boolean hasPurchased(EnumPurchaseType type) {
        if (type == EnumPurchaseType.UNKNOWN)
            return false;
        if (isEverything())
            return true;
        return getPurchase(type) != null;
    }

    public List<AbstractHyperiumPurchase> getPurchases() {
        return purchases;
    }

    public JsonHolder getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "HyperiumPurchase{" +
            "playerUUID=" + playerUUID +
            ", response=" + response +
            '}';
    }

    public AbstractHyperiumPurchase getPurchase(EnumPurchaseType type) {
        for (AbstractHyperiumPurchase purchase : purchases) {
            if (purchase.getType() == type) return purchase;
        }
        return null;
    }
}
