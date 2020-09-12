package net.hypixel.api;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.WebsiteUtils;

public class HypixelApiGuild implements HypixelApiObject {
    private JsonHolder guild;
    public HypixelApiGuild(JsonHolder master) {
        this.guild = master == null ? new JsonHolder() : master;
    }

    @Override
    public JsonHolder getData() {
        return guild;
    }

    public boolean isValid() {
        return guild.has("guild");
    }

    private JsonHolder getRoot() {
        return guild.optJSONObject("guild");
    }

    public String getName() {
        return getRoot().optString("name");
    }

    public boolean isLoaded() {
        return guild.optBoolean("loaded");
    }

    public String getFormatedTag() {
        if (!getTag().isEmpty()) {
            return (getRoot().has("tagColor") ? WebsiteUtils.getColor(getRoot().optString("tagColor")) : "§7") + "[" + getTag() + "]";
        } else {
            return "";
        }
    }

    public String getTag() {
        return getRoot().optString("tag");
    }

    public String getDescription() {
        return getRoot().optString("description");
    }

    public double getLevel() {
        return getRoot().optDouble("level_calc");
    }

    public int getWins() {
        return getRoot().optJSONObject("achievements").optInt("WINNERS");
    }
}
