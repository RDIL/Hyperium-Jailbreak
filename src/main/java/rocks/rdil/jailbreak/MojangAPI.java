package rocks.rdil.jailbreak;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.http.impl.client.HttpClients;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.util.UUID;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class MojangAPI {
    private static String getJson(String url) throws IOException {
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("User-agent", "HyperiumJailbreak");

        // Execute and get the response.
        return EntityUtils.toString(HttpClients.createDefault().execute(httpget).getEntity(), "UTF-8");
    }
    private static final Pattern STRIPPED_UUID_PATTERN = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");
    private static String stripDashes(String uuid) {
        return uuid.replaceAll("-", "");
    }
    private static String addDashes(String uuid) {
        return STRIPPED_UUID_PATTERN.matcher(uuid).replaceAll("$1-$2-$3-$4-$5");
    }
    public static ArrayList<Name> getNameHistory(UUID uuid) throws Exception {
        ArrayList<Name> names = new ArrayList<>();
        Gson gson = new Gson();
        try {
            String json = getJson(String.format("https://api.mojang.com/user/profiles/%s/names", stripDashes(uuid.toString())));
            JsonElement parser = new JsonParser().parse(json);
            if (json.isEmpty()) throw new Exception();
            JsonArray arrayNames = parser.getAsJsonArray();
            arrayNames.forEach(obj -> {
                Name name = gson.fromJson(obj, Name.class);
                names.add(name);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return names;
    }
    public static UUID getUUID(String username) throws Exception {
        String json = getJson("https://api.mojang.com/users/profiles/minecraft/" + username);
        JsonElement parse = new JsonParser().parse(json);
        if (parse.isJsonNull()) throw new Exception();
        JsonObject obj = parse.getAsJsonObject();
        if (obj.get("error") instanceof JsonNull) throw new Exception(obj.get("errorMessage").getAsString());
        return UUID.fromString(addDashes(obj.get("id").getAsString()));
    }
    @SuppressWarnings("unused")
    public static class Name {
        @SerializedName("name") @Expose private String name;
        @SerializedName("changedToAt") @Expose private long changedToAt;

        public String getName() {
            return name;
        }

        public long getChangedToAt() {
            return changedToAt;
        }
    }
}