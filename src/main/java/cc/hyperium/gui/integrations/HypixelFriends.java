package cc.hyperium.gui.integrations;

import cc.hyperium.utils.JsonHolder;
import net.hypixel.api.HypixelApiFriendObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

class HypixelFriends {
    private final List<HypixelApiFriendObject> list = new ArrayList<>();

    HypixelFriends(JsonHolder data) {
        for (String s : data.getKeys()) {
            JsonHolder jsonHolder = data.optJSONObject(s);
            jsonHolder.put("uuid", s);
            list.add(new HypixelApiFriendObject(jsonHolder));
        }
    }

    void sort(FriendSortType type) {
        list.sort(type);
    }

    void removeIf(Predicate<? super HypixelApiFriendObject> e) {
        list.removeIf(e);
    }

    List<HypixelApiFriendObject> get() {
        return list;
    }
}
