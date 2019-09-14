package cc.hyperium.handlers.handlers.stats.fields;

import cc.hyperium.handlers.handlers.stats.AbstractHypixelStats;
import cc.hyperium.handlers.handlers.stats.display.StatsDisplayItem;
import net.hypixel.api.HypixelApiPlayer;
import net.hypixel.api.GameType;
import java.util.List;

public class CrazyWallsStats extends AbstractHypixelStats {
    @Override
    public String getImage() {
        return "CrazyWalls-64";
    }

    @Override
    public String getName() {
        return "Crazy Walls";
    }

    @Override
    public GameType getGameType() {
        return GameType.TRUE_COMBAT;
    }

    @Override
    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
        return super.getPreview(player);
    }
}
