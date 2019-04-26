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

package club.sk1er.website.api.requests;
import cc.hyperium.handlers.handlers.data.HypixelAPI;
import cc.hyperium.utils.JsonHolder;
import club.sk1er.website.utils.WebsiteUtils;
import net.hypixel.api.GameType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;

public class HypixelApiPlayer implements HypixelApiObject {
    public static final DateFormat DMY = new SimpleDateFormat("dd/MM, YYYY");
    public static final DateFormat DMYHHMMSS = new SimpleDateFormat("dd/MM, YYYY HH:mm:ss");
    private final JsonHolder player;
    public HypixelApiPlayer(JsonHolder holder) {
        this.player = holder;
    }
    public int getKarma() {
        return getRoot().optInt("karma");
    }
    public int getNetworkLevel() {
        return getRoot().optInt("networkLevel") + 1;
    }
    public boolean isValid() {
        return player != null && !player.isNull("player") && player.has("player");
    }

    @Override
    public JsonHolder getData() {
        return player;
    }

    public String getUUID() {
        return getRoot().optString("uuid");
    }
    public JsonHolder getRoot() {
        return player.optJSONObject("player");
    }

    public JsonHolder getStats() {
        return getRoot().optJSONObject("stats");
    }

    public JsonHolder getStats(GameType type) {
        return getStats().optJSONObject(type.getDbName());
    }

    public String getName() {
        return getRoot().optString("displayname");
    }

    public JsonHolder getGiftMeta() {
        return getRoot().optJSONObject("giftingMeta");
    }

    public int getAchievementPoints() {
        return getRoot().optInt("points");
    }

    public HypixelApiGuild getGuild() {
        try {
            return HypixelAPI.INSTANCE.getGuildFromPlayer(getUUID()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getTotalCoins() {
        return getRoot().optInt("coins");
    }

    public int getTotalKills() {
        return getRoot().optInt("kills");
    }

    public int getTotalWins() {
        return getRoot().optInt("wins");
    }

    public boolean has(String val) {
        return getRoot().has(val);
    }

    public GameType mostRecentGame() {
        return GameType.parse(getRoot().optString("mostRecentGameType"));
    }

    private boolean isYouTuber() {
        return getRoot().optString("rank").equalsIgnoreCase("youtuber");
    }

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
        }
        else if (getRoot().has("packageRank")) {
            return getRoot().optString("packageRank");
        }
        return "NONE";
    }

    public String getDisplayString() {
        return getRoot().optString("display");
    }

    public Rank getRank() {
        return Rank.get(getRankForMod().toUpperCase());
    }

    public int getFriendCount() {
        return getRoot().optInt("friends");
    }

    public long getInt(String path) {
        return WebsiteUtils.get(getRoot().getObject(), path);
    }

    enum Rank {
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
        static Rank get(String in) {
            for (Rank rank : values()) {
                if (rank.name().equalsIgnoreCase(in))
                    return rank;
            }
            return NONE;
        }
    }
}
