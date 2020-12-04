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

import cc.hyperium.utils.JsonHolder;

public class HypixelApiPlayer implements HypixelApiObject {
    private final JsonHolder player;

    public HypixelApiPlayer(JsonHolder holder) {
        this.player = holder;
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

    public String getName() {
        return getRoot().optString("displayname");
    }

    public boolean has(String val) {
        return getRoot().has(val);
    }

    private boolean isYouTuber() {
        return getRoot().optString("rank").equalsIgnoreCase("youtuber");
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

    public String getDisplayString() {
        return getRoot().optString("display");
    }

    public Rank getRank() {
        return Rank.get(getRankForMod().toUpperCase());
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
