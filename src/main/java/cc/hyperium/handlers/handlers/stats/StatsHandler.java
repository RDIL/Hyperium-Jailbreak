package cc.hyperium.handlers.handlers.stats;

import cc.hyperium.handlers.handlers.data.HypixelAPI;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import club.sk1er.website.api.requests.HypixelApiGuild;
import club.sk1er.website.api.requests.HypixelApiPlayer;

public class StatsHandler {
    public void initStatsViewer(String player) {
        Multithreading.runAsync(() -> {
            try {
                HypixelApiPlayer apiPlayer = HypixelAPI.INSTANCE.getPlayer(player).get();

                new PlayerStatsGui(apiPlayer).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public void loadGuildByPlayer(String player) {
        Multithreading.runAsync(() -> {
            try {
                HypixelApiGuild apiGuild = HypixelAPI.INSTANCE.getGuildFromPlayer(player).get();

                new GuildStatsGui(apiGuild).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public void loadGuildByName(String name) {
        Multithreading.runAsync(() -> {
            try {
                HypixelApiGuild apiGuild = HypixelAPI.INSTANCE.getGuildFromName(name).get();
                new GuildStatsGui(apiGuild).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
