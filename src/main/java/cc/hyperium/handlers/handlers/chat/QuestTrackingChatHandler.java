package cc.hyperium.handlers.handlers.chat;

import cc.hyperium.Hyperium;
import com.google.common.io.Files;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.IChatComponent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class QuestTrackingChatHandler extends HyperiumChatHandler {
    private final File file;
    private JsonArray json;

    public QuestTrackingChatHandler() {
        file = new File(Hyperium.folder, "_quest_.json");
        if (!file.exists()) {
            json = new JsonArray();
            save();
        }
        load();
    }

    @Override
    public boolean chatReceived(IChatComponent component, String text) {
        Matcher matcher = regexPatterns.get(ChatRegexType.QUEST_COMPLETE).matcher(text);
        if (matcher.matches()) {
            JsonObject record = new JsonObject();
            record.add("name", new JsonPrimitive(matcher.group("name")));
            record.add("type", new JsonPrimitive(matcher.group("type")));
            record.add("timestamp", new JsonPrimitive(System.currentTimeMillis()));
            json.add(record);
            save();
        }
        return false;
    }

    private void load() {
        try {
            json = new JsonParser().parse(Files.toString(file, Charset.defaultCharset())).getAsJsonArray();
        } catch (IOException e) {
            if (json == null)
                json = new JsonArray();
            e.printStackTrace();
        }
    }

    private void save() {
        try {
            Files.write(json.toString(), file, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
            Hyperium.LOGGER.error("Could not save quest tracking json to the file!");
        }
    }

    public List<QuestData> getTrackedQuests() {
        List<QuestData> trackedQuests = new ArrayList<>();
        load();
        json.forEach(e -> {
            JsonObject o = e.getAsJsonObject();
            trackedQuests.add(new QuestData(o.get("name").getAsString(), o.get("type").getAsString(), o.get("timestamp").getAsLong()));
        });
        return trackedQuests;
    }

    public class QuestData {
        private final String name;
        private final String type;
        private final Long timestamp;

        public QuestData(String name, String type, Long timestamp) {
            this.name = name;
            this.type = type;
            this.timestamp = timestamp;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public Long getTimestamp() {
            return timestamp;
        }
    }
}
