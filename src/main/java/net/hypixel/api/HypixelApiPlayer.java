/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.hypixel.api;

import cc.hyperium.handlers.handlers.data.HypixelAPI;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.WebsiteUtils;
import java.util.concurrent.ExecutionException;

/**
 * A Hypixel player, as represented by the API.
 */
public class HypixelApiPlayer implements HypixelApiObject {
    private final JsonHolder player;

    /**
     * Creates a new player object based on the specified JSON.
     *
     * @param holder The JSON.
     */
    public HypixelApiPlayer(JsonHolder holder) {
        this.player = holder;
    }

    /**
     * Get the number of karma the player has.
     *
     * @return The player's karma amount.
     */
    public int getKarma() {
        return getRoot().optInt("karma");
    }

    /**
     * Get the player's network level.
     *
     * @return The player's network level.
     */
    public int getNetworkLevel() {
        return getRoot().optInt("networkLevel") + 1;
    }

    /**
     * If the player is valid or not.
     *
     * @return The validity status of the player.
     */
    public boolean isValid() {
        return player != null && !player.isNull("player") && player.has("player");
    }

    @Override
    public JsonHolder getData() {
        return player;
    }

    /**
     * Get the UUID of the player.
     *
     * @return The player's UUID.
     */
    public String getUUID() {
        return getRoot().optString("uuid");
    }

    private JsonHolder getRoot() {
        return player.optJSONObject("player");
    }

    /**
     * Get the JSON object with the player's stats.
     *
     * @return The player's stats, in a JSON object.
     */
    public JsonHolder getStats() {
        return getRoot().optJSONObject("stats");
    }

    /**
     * Get the stats for the player in the specified {@link net.hypixel.api.GameType}.
     *
     * @param type The {@link net.hypixel.api.GameType}.
     * @return The player's stats, as a JSON object.
     */
    public JsonHolder getStats(GameType type) {
        return getStats().optJSONObject(type.getDbName());
    }

    /**
     * Get the player's name.
     *
     * @return The player's name.
     */
    public String getName() {
        return getRoot().optString("displayname");
    }

    /**
     * Get the gift stats for the player.
     *
     * @return The player's gift stats, as a JSON object.
     */
    public JsonHolder getGiftMeta() {
        return getRoot().optJSONObject("giftingMeta");
    }

    /**
     * Get the number of achivement points the player has.
     *
     * @return The number of achivement points the player has.
     */
    public int getAchievementPoints() {
        return getRoot().optInt("points");
    }

    /**
     * Get the player's guild, as a {@link net.hypixel.api.HypixelApiGuild} object.
     *
     * @return The player's guild.
     */
    public HypixelApiGuild getGuild() {
        try {
            return HypixelAPI.INSTANCE.getGuildFromPlayer(getUUID()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get the player's total number of coins.
     *
     * @return The player's total number of coins.
     */
    public int getTotalCoins() {
        return getRoot().optInt("coins");
    }

    /**
     * Get the player's total number of kills.
     *
     * @return The player's total number of kills.
     */
    public int getTotalKills() {
        return getRoot().optInt("kills");
    }

    /**
     * Get the player's total number of wins.
     *
     * @return The player's total number of wins.
     */
    public int getTotalWins() {
        return getRoot().optInt("wins");
    }

    public boolean has(String val) {
        return getRoot().has(val);
    }

    /**
     * Get the player's most recent game type.
     *
     * @return The player's most recent game type.
     */
    public GameType mostRecentGame() {
        return GameType.parse(getRoot().optString("mostRecentGameType"));
    }

    private boolean isYouTuber() {
        return getRoot().optString("rank").equalsIgnoreCase("youtuber");
    }

    /**
     * Get if the player is Hypixel staff or has [YOUTUBE] rank.
     *
     * @return If the player is Hypixel staff or has [YOUTUBE] rank.
     */
    public boolean isStaffOrYT() {
        return isStaff() || isYouTuber();
    }

    private boolean isStaff() {
        String rank = getRoot().optString("rank");
        return rank.equalsIgnoreCase("admin") || rank.equalsIgnoreCase("moderator") || rank.equalsIgnoreCase("helper");
    }

    private String getRankForMod() {
        if (isStaff() || isYouTuber()) {
            String string = getRoot().optString("rank");
            if (!string.equalsIgnoreCase("normal")) {
                return string;
            }
        } else if (getRoot().has("newPackageRank")) {
            return getRoot().optString("newPackageRank");
        } else if (getRoot().has("packageRank")) {
            return getRoot().optString("packageRank");
        }
        return "NONE";
    }

    /**
     * Get the player's display string.
     *
     * @return The player's display string.
     */
    public String getDisplayString() {
        return getRoot().optString("display");
    }

    /**
     * Get the player's {@link net.hypixel.api.HypixelApiPlayer.Rank}.
     *
     * @return The player's {@link net.hypixel.api.HypixelApiPlayer.Rank}.
     */
    public Rank getRank() {
        return Rank.get(getRankForMod().toUpperCase());
    }

    /**
     * Get the number of friends the player has.
     *
     * @return The number of friends the player has.
     */
    public int getFriendCount() {
        return getRoot().optInt("friends");
    }

    public long getInt(String path) {
        return WebsiteUtils.get(getRoot().getObject(), path);
    }

    /**
     * An enum representing the Hypixel ranks.
     */
    public enum Rank {
        ADMIN,
        MODERATOR,
        HELPER,
        YOUTUBER,
        MVP_PLUS_PLUS,
        MVP_PLUS,
        MVP,
        VIP_PLUS,
        VIP,
        NONE;

        /**
         * Get the enum value of the rank specified.
         *
         * @param in The rank value.
         * @return The rank's enum value.
         */
        public static Rank get(String in) {
            for (Rank rank : values()) {
                if (rank.name().equalsIgnoreCase(in))
                    return rank;
            }
            return NONE;
        }
    }
}
