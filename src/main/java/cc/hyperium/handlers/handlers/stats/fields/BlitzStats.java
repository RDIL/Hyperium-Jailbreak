package cc.hyperium.handlers.handlers.stats.fields;

import cc.hyperium.handlers.handlers.stats.AbstractHypixelStats;
import cc.hyperium.handlers.handlers.stats.display.DisplayLine;
import cc.hyperium.handlers.handlers.stats.display.StatsDisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.hypixel.api.HypixelApiPlayer;
import cc.hyperium.network.WebsiteUtils;
import net.hypixel.api.GameType;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class BlitzStats extends AbstractHypixelStats {
    @Override
    public String getImage() {
        return "SG-64";
    }

    @Override
    public String getName() {
        return GameType.SURVIVAL_GAMES.getName();
    }

    @Override
    public GameType getGameType() {
        return GameType.SURVIVAL_GAMES;
    }

    @Override
    public List<StatsDisplayItem> getPreview(HypixelApiPlayer player) {
        ArrayList<StatsDisplayItem> items = new ArrayList<>();
        JsonHolder stats = player.getStats(GameType.SURVIVAL_GAMES);
        items.add(new DisplayLine(bold("Coins: ", stats.optInt("coins")), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("Kills: ", stats.optInt("kills")), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("Deaths: ", stats.optInt("deaths")), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("Wins: ", stats.optInt("wins")), Color.WHITE.getRGB()));
        items.add(new DisplayLine(bold("K/D: ", WebsiteUtils.buildRatio(stats.optInt("kills"), stats.optInt("deaths"))), Color.WHITE.getRGB()));
        return items;
    }

    @Override
    public List<StatsDisplayItem> getDeepStats(HypixelApiPlayer player) {
        return getPreview(player);
    }
}
