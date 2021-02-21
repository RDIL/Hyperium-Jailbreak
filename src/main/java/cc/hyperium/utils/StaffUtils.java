package cc.hyperium.utils;

import cc.hyperium.utils.staff.StaffSettings;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;
import cc.hyperium.installer.utils.http.client.HttpClient;
import cc.hyperium.installer.utils.http.client.methods.HttpGet;
import cc.hyperium.installer.utils.http.impl.client.HttpClients;
import org.apache.commons.io.IOUtils;

public class StaffUtils {
    public static HttpClient httpclient = HttpClients.createDefault();

    private static final HashMap<UUID, StaffSettings> STAFF_CACHE = new HashMap<>();
    public static boolean isStaff(UUID uuid) {
        return STAFF_CACHE.containsKey(uuid);
    }

    public static DotColour getColor(UUID uuid) {
        return STAFF_CACHE.get(uuid).getColour();
    }

    public static String getRawText(final String url) {
        String s = "";
        try {
            s = IOUtils.toString(httpclient.execute(new HttpGet(url)).getEntity().getContent(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    private static HashMap<UUID, StaffSettings> getStaff() throws IOException {
        HashMap<UUID, StaffSettings> staff = new HashMap<>();
        String content = getRawText("https://raw.githubusercontent.com/hyperiumjailbreak/tools/master/staff.json");
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(content).getAsJsonArray();
        for (int i = 0; i < array.size(); i++) {
            JsonObject item = array.get(i).getAsJsonObject();
            UUID uuid = UUID.fromString(item.get("uuid").getAsString());
            String colourStr = item.get("color").getAsString().toUpperCase();
            DotColour colour;
            if (colourStr.equals("CHROMA")) {
                colour = new DotColour(true, ChatColor.WHITE);
            } else {
                colour = new DotColour(false, ChatColor.valueOf(colourStr));
            }
            staff.put(uuid, new StaffSettings(colour));
        }
        return staff;
    }

    public static void clearCache() {
        STAFF_CACHE.clear();
        Multithreading.runAsync(() -> {
            try {
                STAFF_CACHE.putAll(getStaff());
            } catch (IOException ignored) {}
        });
    }

    public static class DotColour {
        public boolean isChroma;
        public ChatColor baseColour;

        public DotColour(boolean isChroma, ChatColor baseColour) {
            this.isChroma = isChroma;
            this.baseColour = baseColour;
        }
    }
}
