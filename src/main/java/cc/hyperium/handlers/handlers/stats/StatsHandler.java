package cc.hyperium.handlers.handlers.stats;

import cc.hyperium.mods.sk1ercommon.Multithreading;

public class StatsHandler {
    public void initStatsViewer() {
        Multithreading.runAsync(() -> {
            try {
                new PlayerStatsGui().show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
