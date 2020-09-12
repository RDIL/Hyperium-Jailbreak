package cc.hyperium.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public class JsonHolder {
    private JsonObject object;
    private boolean parsedCorrectly;

    public JsonHolder(JsonObject object) {
        this.parsedCorrectly = true;
        this.object = object;
    }

    public JsonHolder(String raw) {
        this.parsedCorrectly = true;
        if (raw != null && !raw.isEmpty()) {
            try {
                this.object = (new JsonParser()).parse(raw).getAsJsonObject();
            } catch (Exception var3) {
                this.object = new JsonObject();
                this.parsedCorrectly = false;
                var3.printStackTrace();
            }
        } else {
            this.object = new JsonObject();
        }
    }

    public JsonHolder() {
        this(new JsonObject());
    }

    public boolean isParsedCorrectly() {
        return this.parsedCorrectly;
    }

    public String toString() {
        return this.object != null ? this.object.toString() : "{}";
    }

    public JsonHolder put(String key, boolean value) {
        this.object.addProperty(key, value);
        return this;
    }

    public void mergeNotOverride(JsonHolder merge) {
        this.merge(merge, false);
    }

    public void mergeOverride(JsonHolder merge) {
        this.merge(merge, true);
    }

    public void merge(JsonHolder merge, boolean override) {
        JsonObject object = merge.getObject();
        Iterator var4 = merge.getKeys().iterator();

        while(true) {
            String s;
            do {
                if (!var4.hasNext()) {
                    return;
                }

                s = (String)var4.next();
            } while(!override && this.has(s));

            this.put(s, object.get(s));
        }
    }

    private void put(String s, JsonElement element) {
        this.object.add(s, element);
    }

    public JsonHolder put(String key, String value) {
        this.object.addProperty(key, value);
        return this;
    }

    public JsonHolder put(String key, int value) {
        this.object.addProperty(key, value);
        return this;
    }

    public JsonHolder put(String key, double value) {
        this.object.addProperty(key, value);
        return this;
    }

    public JsonHolder put(String key, long value) {
        this.object.addProperty(key, value);
        return this;
    }

    public JsonHolder optJSONObject(String key, JsonObject fallBack) {
        try {
            return new JsonHolder(this.object.get(key).getAsJsonObject());
        } catch (Exception var4) {
            return new JsonHolder(fallBack);
        }
    }

    public JsonArray optJSONArray(String key, JsonArray fallback) {
        try {
            return this.object.get(key).getAsJsonArray();
        } catch (Exception var4) {
            return fallback;
        }
    }

    public JsonArray optJSONArray(String key) {
        return this.optJSONArray(key, new JsonArray());
    }

    public boolean has(String key) {
        return this.object.has(key);
    }

    public long optLong(String key, long fallback) {
        try {
            return this.object.get(key).getAsLong();
        } catch (Exception var5) {
            return fallback;
        }
    }

    public long optLong(String key) {
        return this.optLong(key, 0L);
    }

    public boolean optBoolean(String key, boolean fallback) {
        try {
            return this.object.get(key).getAsBoolean();
        } catch (Exception var4) {
            return fallback;
        }
    }

    public boolean optBoolean(String key) {
        return this.optBoolean(key, false);
    }

    public JsonObject optActualJSONObject(String key) {
        try {
            return this.object.get(key).getAsJsonObject();
        } catch (Exception var3) {
            return new JsonObject();
        }
    }

    public JsonHolder optJSONObject(String key) {
        return this.optJSONObject(key, new JsonObject());
    }

    public int optInt(String key, int fallBack) {
        try {
            return this.object.get(key).getAsInt();
        } catch (Exception var4) {
            return fallBack;
        }
    }

    public int optInt(String key) {
        return this.optInt(key, 0);
    }

    public String optString(String key, String fallBack) {
        try {
            return this.object.get(key).getAsString();
        } catch (Exception var4) {
            return fallBack;
        }
    }

    public String optString(String key) {
        return this.optString(key, "");
    }

    public double optDouble(String key, double fallBack) {
        try {
            return this.object.get(key).getAsDouble();
        } catch (Exception var5) {
            return fallBack;
        }
    }

    public List<String> getKeys() {
        return (List)this.object.entrySet().stream().map(Entry::getKey).collect(Collectors.toList());
    }

    public double optDouble(String key) {
        return this.optDouble(key, 0.0D);
    }

    public JsonObject getObject() {
        return this.object;
    }

    public boolean isNull(String key) {
        return this.object.has(key) && this.object.get(key).isJsonNull();
    }

    public JsonHolder put(String values, JsonHolder values1) {
        return this.put(values, values1.getObject());
    }

    public JsonHolder put(String values, JsonObject object) {
        this.object.add(values, object);
        return this;
    }

    public void putArray(String blacklisted, JsonArray jsonElements) {
        this.object.add(blacklisted, jsonElements);
    }

    public void remove(String header) {
        this.object.remove(header);
    }

    public List<String> getJsonArrayAsStringList(String root) {
        ArrayList strings = new ArrayList();

        try {
            Iterator var3 = this.object.get(root).getAsJsonArray().iterator();

            while(var3.hasNext()) {
                JsonElement element = (JsonElement)var3.next();
                strings.add(element.getAsString());
            }
        } catch (Exception var5) {
        }

        return strings;
    }
}
