package net.hypixel.api;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonArray;

public class HypixelApiFriends implements HypixelApiObject {
    private JsonHolder master;

    public HypixelApiFriends(JsonHolder o) {
        if (o != null) {
            this.master = o;
        } else {
            master = new JsonHolder();
        }
    }

    @Override
    public String toString() {
        return master.toString();
    }

    @Override
    public JsonHolder getData() {
        return master;
    }

    public boolean isValid() {
        return master != null && !master.isNull("records");
    }

    public JsonArray getFriends() {
        return master.optJSONArray("records");
    }
}
