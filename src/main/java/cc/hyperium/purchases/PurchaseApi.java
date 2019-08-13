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

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.PurchaseLoadEvent;
import cc.hyperium.event.SpawnpointChangeEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.purchases.packages.EarsCosmetic;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class PurchaseApi {
    public static final String url = "https://api.hyperium.cc/purchases/";
    private static final PurchaseApi instance = new PurchaseApi();
    private final Map<UUID, HyperiumPurchase> purchasePlayers = new ConcurrentHashMap<>();
    private final Map<EnumPurchaseType, Class<? extends AbstractHyperiumPurchase>> purchaseClasses = new HashMap<>();

    private PurchaseApi() {
        register(EnumPurchaseType.DEADMAU5_COSMETIC, EarsCosmetic.class);

        for (EnumPurchaseType enumPurchaseType : EnumPurchaseType.values()) {
            purchaseClasses.putIfAbsent(enumPurchaseType, DefaultCosmetic.class);
        }
        getSelf();
    }

    public static PurchaseApi getInstance() {
        return instance;
    }

    @InvokeEvent
    public void worldSwitch(SpawnpointChangeEvent event) {
        Multithreading.runAsync(() -> {
            synchronized (instance) {
                UUID id = UUIDUtil.getClientUUID();
                if (id == null) {
                    return;
                }
                HyperiumPurchase purchase = purchasePlayers.get(id);
                purchasePlayers.clear();
                if (purchase != null) {
                    purchasePlayers.put(id, purchase);
                }
            }
        });
    }

    public HyperiumPurchase getPackageSync(UUID uuid) {
        if (uuid == null)
            return null;
        return purchasePlayers.computeIfAbsent(uuid, uuid1 -> {
            String s = uuid.toString().replace("-", "");
            if (s.length() == 32 && s.charAt(12) != '4') {

                HyperiumPurchase non_player = new HyperiumPurchase(uuid, new JsonHolder().put("non_player", true));
                EventBus.INSTANCE.post(new PurchaseLoadEvent(uuid, non_player));
                return non_player;
            }

            HyperiumPurchase hyperiumPurchase = new HyperiumPurchase(uuid, get(url + uuid.toString()));
            EventBus.INSTANCE.post(new PurchaseLoadEvent(uuid, hyperiumPurchase));
            return hyperiumPurchase;
        });
    }

    public HyperiumPurchase getPackageIfReady(UUID uuid) {
        if (uuid == null)
            return null;
        return purchasePlayers.get(uuid);
    }

    public void getPackageAsync(UUID uuid, Consumer<HyperiumPurchase> callback) {
        try {
            Multithreading.runAsync(() -> callback.accept(getPackageSync(uuid)));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public HyperiumPurchase getSelf() {
        return getPackageIfReady(UUIDUtil.getClientUUID());
    }

    public void register(EnumPurchaseType type, Class<? extends AbstractHyperiumPurchase> ex) {
        purchaseClasses.put(type, ex);
    }

    public AbstractHyperiumPurchase parse(EnumPurchaseType type, JsonHolder data) {
        Class<? extends AbstractHyperiumPurchase> c = purchaseClasses.get(type);
        if (c == null) {
            return null;
        }
        Class[] cArg = new Class[2];
        cArg[0] = EnumPurchaseType.class;
        cArg[1] = JsonHolder.class;
        try {
            Constructor<? extends AbstractHyperiumPurchase> declaredConstructor = c.getDeclaredConstructor(cArg);
            return declaredConstructor.newInstance(type, data);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JsonHolder get(String url) {
        url = url.replace(" ", "%20");
        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(true);
            connection.addRequestProperty("User-Agent", "Mozilla/4.76 Hyperium ");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoOutput(true);
            InputStream is = connection.getInputStream();
            return new JsonHolder(IOUtils.toString(is, Charset.forName("UTF-8")));
        } catch (Exception ignored) {}
        JsonObject object = new JsonObject();
        object.addProperty("success", false);
        object.addProperty("cause", "Exception");
        return new JsonHolder(object);
    }

    public synchronized void refreshSelf() {
        UUID id = UUIDUtil.getClientUUID();
        HyperiumPurchase value = new HyperiumPurchase(id, get(url + id.toString()));
        EventBus.INSTANCE.post(new PurchaseLoadEvent(id, value));
        purchasePlayers.put(id, value);
    }

    public void reload(UUID uuid) {
        HyperiumPurchase value = new HyperiumPurchase(uuid, get(url + uuid.toString()));
        EventBus.INSTANCE.post(new PurchaseLoadEvent(uuid, value));
        purchasePlayers.put(uuid, value);
        Hyperium.INSTANCE.getHandlers().getCapeHandler().deleteCape(uuid);
    }
}
