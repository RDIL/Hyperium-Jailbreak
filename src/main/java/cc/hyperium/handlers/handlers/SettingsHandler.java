package cc.hyperium.handlers.handlers;

import cc.hyperium.config.Settings;
import cc.hyperium.gui.ParticleOverlay;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.JsonHolder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SettingsHandler {
    @SuppressWarnings("unused") public int dummy = 0;
    private HashMap<Field, Supplier<String[]>> customStates = new HashMap<>();
    private HashMap<Field, List<Consumer<Object>>> callbacks = new HashMap<>();
    private List<Object> settingsObjects = new ArrayList<>();

    public SettingsHandler() {
        try {
            String[] hats1 = new String[]{"Tophat", "Fez", "Lego"};
            EnumPurchaseType[] hat2 = new EnumPurchaseType[]{EnumPurchaseType.HAT_TOPHAT, EnumPurchaseType.HAT_FEZ, EnumPurchaseType.HAT_LEGO};

            Field hats = Settings.class.getField("HAT_TYPE");
            customStates.put(hats, () -> {
                HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
                if (self != null) {
                    List<String> pur = new ArrayList<>();
                    for (int i = 0; i < hat2.length; i++) {
                        if (self.hasPurchased(hat2[i])) {
                            pur.add(hats1[i]);
                        }
                    }
                    if (pur.size() > 0) {
                        pur.add("NONE");
                        String[] tmp = new String[pur.size()];
                        for (int i = 0; i < pur.size(); i++) {
                            tmp[i] = pur.get(i);
                        }
                        return tmp;
                    }
                }
                return new String[]{"NOT PURCHASED"};
            });
            registerCallback(hats, o -> {
                NettyClient client = NettyClient.getClient();
                if (client == null) return;
                EnumPurchaseType parse = null;
                for (int i = 0; i < hats1.length; i++) {
                    if (hats1[i].equalsIgnoreCase(o.toString())) {
                        parse = hat2[i];
                    }
                }
                boolean none = o.toString().equalsIgnoreCase("NONE");
                if (parse == null && !none) {
                    GeneralChatHandler.instance().sendMessage("Unable to locate hat type: " + o);
                    return;
                }
                JsonHolder put = new JsonHolder().put("internal", true).put("set_hat", true).put("value", none ? "NONE" : parse.toString());
                ServerCrossDataPacket build = ServerCrossDataPacket.build(put);
                client.write(build);
            });
            registerCallback(this.getClass().getField("dummy"), o -> {
                NettyClient client = NettyClient.getClient();
                if (client == null) {
                    return;
                }
                JsonHolder put = new JsonHolder().put("internal", true).put("companion", true).put("type", "NONE");
                ServerCrossDataPacket build = ServerCrossDataPacket.build(put);
                client.write(build);
            });

            Field max_particle_string = Settings.class.getField("MAX_PARTICLE_STRING");
            customStates.put(max_particle_string, () -> {
                ParticleOverlay overlay = ParticleOverlay.getOverlay();
                if (overlay.purchased()) {
                    return new String[]{
                        "25",
                        "50",
                        "100",
                        "150",
                        "200",
                        "250",
                        "300"};
                }
                return new String[]{"NOT PURCHASED"};
            });

            Field show_wings_string = Settings.class.getField("SHOW_WINGS");
            customStates.put(show_wings_string, () -> {
                HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
                if (self != null && self.hasPurchased(EnumPurchaseType.WING_COSMETIC)) {
                    return new String[]{
                        "ON",
                        "OFF"
                    };
                }

                return new String[]{"NOT PURCHASED"};
            });

            registerCallback(show_wings_string, o -> {
                try {
                    Settings.SHOW_WINGS = String.valueOf(o);
                    // Update on netty.
                    String update = String.valueOf(o);
                    boolean packetUpdate;

                    packetUpdate = update.equalsIgnoreCase("on");

                    ServerCrossDataPacket packet = ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("wings_toggle", packetUpdate));

                    NettyClient client = NettyClient.getClient();
                    if (client != null) {
                        client.write(packet);
                    }
                } catch (Exception ignored) {

                }
            });

            Field show_dragonhead_string = Settings.class.getField("SHOW_DRAGON_HEAD");
            customStates.put(show_dragonhead_string, () -> {
                HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
                if (self != null && self.hasPurchased(EnumPurchaseType.DRAGON_HEAD)) {
                    return new String[]{
                        "ON",
                        "OFF"
                    };
                }

                return new String[]{"NOT PURCHASED"};
            });
            registerCallback(show_dragonhead_string, o -> {
                try {
                    Settings.SHOW_DRAGON_HEAD = String.valueOf(o);
                    String update = String.valueOf(o);

                    boolean packetUpdate;
                    // Update on netty.
                    packetUpdate = update.equalsIgnoreCase("on");

                    ServerCrossDataPacket packet = ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("dragon_head", packetUpdate));

                    NettyClient client = NettyClient.getClient();
                    if (client != null) {
                        client.write(packet);
                    }
                } catch (Exception ignored) {

                }
            });

            registerCallback(Settings.class.getField("MAX_WORLD_PARTICLES_STRING"), o -> {
                try {
                    Settings.MAX_WORLD_PARTICLES_INT = Integer.parseInt(o.toString());
                } catch (Exception ignored) {

                }
            });
            registerCallback(max_particle_string, o -> {
                try {
                    Settings.MAX_PARTICLES = Integer.parseInt(o.toString());
                } catch (Exception ignored) {
                }
            });
            registerCallback(Settings.class.getField("HEAD_SCALE_FACTOR_STRING"), o -> {
                try {
                    Settings.HEAD_SCALE_FACTOR = Double.parseDouble(o.toString());
                } catch (Exception ignored) {
                }
            });
            Field flip_type_string = Settings.class.getField("FLIP_TYPE_STRING");
            customStates.put(flip_type_string, () -> {
                HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
                if (self == null || !self.hasPurchased(EnumPurchaseType.FLIP_COSMETIC))
                    return new String[]{"NOT PURCHASED"};
                else return new String[]{"FLIP", "ROTATE"};
            });
            registerCallback(flip_type_string, o -> {
                String s = o.toString();
                if (s.equalsIgnoreCase("FLIP")) {
                    Settings.flipType = 1;
                } else if (s.equalsIgnoreCase("ROTATE")) {
                    Settings.flipType = 2;
                }
            });
            registerCallback(Settings.class.getField("WINGS_SCALE"), o -> {
                if (PurchaseApi.getInstance() == null || PurchaseApi.getInstance().getSelf() == null || PurchaseApi.getInstance().getSelf().getPurchaseSettings() == null) {
                    return;
                }
                Double o1 = (Double) o;
                HyperiumPurchase self = PurchaseApi.getInstance().getSelf();
                if (self == null) {
                    GeneralChatHandler.instance().sendMessage("Error: Could not update cosmetic state because your purchase profile is not loaded.");
                    return;
                }
                JsonHolder purchaseSettings = self.getPurchaseSettings();
                if (!purchaseSettings.has("wings"))
                    purchaseSettings.put("wings", new JsonHolder());
                purchaseSettings.optJSONObject("wings").put("scale", o1);
                Settings.WINGS_SCALE = o1;
                NettyClient client = NettyClient.getClient();
                if (client != null)
                    client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("wings_scale", o1.doubleValue())));
            });

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void registerCallback(Field field, Consumer<Object> callback) {
        this.callbacks.computeIfAbsent(field, tmp -> new ArrayList<>()).add(callback);
    }

    public HashMap<Field, Supplier<String[]>> getCustomStates() {
        return customStates;
    }

    public HashMap<Field, List<Consumer<Object>>> getcallbacks() {
        return callbacks;
    }

    public List<Object> getSettingsObjects() {
        return settingsObjects;
    }
}
