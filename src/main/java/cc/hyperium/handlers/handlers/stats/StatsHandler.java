package cc.hyperium.handlers.handlers.stats;

import cc.hyperium.handlers.handlers.data.HypixelAPI;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import net.hypixel.api.HypixelApiPlayer;

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
}
