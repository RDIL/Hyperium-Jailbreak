package net.hypixel.api;

import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.WebsiteUtils;

/**
 * A Hypixel guild.
 */
public class HypixelApiGuild implements HypixelApiObject {
    private JsonHolder guild;

    /**
     * Makes a new guild object from the specified JSON object.
     *
     * @param master The JSON object.
     */
    public HypixelApiGuild(JsonHolder master) {
        this.guild = master == null ? new JsonHolder() : master;
    }

    @Override
    public JsonHolder getData() {
        return guild;
    }

    /**
     * Get if the guild is valid or not.
     *
     * @return The validity status.
     */
    public boolean isValid() {
        return guild.has("guild");
    }

    private JsonHolder getRoot() {
        return guild.optJSONObject("guild");
    }

    /**
     * Get the guild's name.
     *
     * @return The guild's name.
     */
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

    /**
     * Get the raw guild tag.
     *
     * @return The raw guild tag.
     */
    public String getTag() {
        return getRoot().optString("tag");
    }

    /**
     * Get the guild's description.
     *
     * @return The guild's description.
     */
    public String getDescription() {
        return getRoot().optString("description");
    }

    /**
     * Get the guild's level.
     *
     * @return The guild's level.
     */
    public double getLevel() {
        return getRoot().optDouble("level_calc");
    }

    /**
     * Get the number of wins the guild has.
     *
     * @return The number of wins the guild has.
     */
    public int getWins() {
        return getRoot().optJSONObject("achievements").optInt("WINNERS");
    }
}
