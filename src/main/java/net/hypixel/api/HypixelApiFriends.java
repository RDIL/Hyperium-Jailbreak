package net.hypixel.api;

import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonArray;

/**
 * A collection of Hypixel friends.
 */
public class HypixelApiFriends implements HypixelApiObject {
    private final JsonHolder master;

    /**
     * Create a new friend holder instance from the specified JSON.
     *
     * @param o The JSON data.
     */
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

    /**
     * Get if the object is valid or not.
     *
     * @return The validity status.
     */
    public boolean isValid() {
        return master != null && !master.isNull("records");
    }

    /**
     * Get the friends tied to this object, in a JSON array object.
     *
     * @return The friends list.
     */
    public JsonArray getFriends() {
        return master.optJSONArray("records");
    }
}
