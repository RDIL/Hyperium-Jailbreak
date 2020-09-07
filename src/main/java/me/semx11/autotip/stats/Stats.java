package me.semx11.autotip.stats;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.config.GlobalSettings;
import me.semx11.autotip.config.GlobalSettings.GameAlias;
import me.semx11.autotip.config.GlobalSettings.GameGroup;
import me.semx11.autotip.gson.exclusion.Exclude;

public abstract class Stats {
    @Exclude
    protected final Autotip autotip;
    @Exclude
    protected final GlobalSettings settings;

    private int tipsSent = 0;
    private int tipsReceived = 0;
    private int xpSent = 0;
    private int xpReceived = 0;

    private Map<String, Coins> gameStatistics = new ConcurrentHashMap<>();

    Stats(Autotip autotip) {
        this.autotip = autotip;
        this.settings = autotip.getGlobalSettings();
    }

    public int getTipsTotalInt() {
        return tipsSent + tipsReceived;
    }

    public void addTipsSent(int tips) {
        this.tipsSent += tips;
    }

    public void addXpTipsSent(int xp) {
        this.addXpSent(xp);
        this.addTipsSent(xp / settings.getXpPerTipSent());
    }

    public void addTipsReceived(int tips) {
        this.tipsReceived += tips;
    }

    public void addXpTipsReceived(int xp) {
        this.addXpReceived(xp);
        this.addTipsReceived(xp / settings.getXpPerTipReceived());
    }

    public void addXpSent(int xp) {
        this.xpSent += xp;
    }

    public void addXpReceived(int xp) {
        this.xpReceived += xp;
    }

    public void addCoinsSent(String game, int coins) {
        this.addCoins(game, coins, 0);
    }

    public void addCoinsReceived(String game, int coins) {
        this.addCoins(game, 0, coins);
    }

    private void addCoins(String game, int coinsSent, int coinsReceived) {
        this.addCoins(game, new Coins(coinsSent, coinsReceived));
    }

    private void addCoins(String game, Coins coins) {
        coins = new Coins(coins);
        for (GameGroup group : settings.getGameGroups()) {
            if (game.equals(group.getName())) {
                for (String groupGame : group.getGames()) {
                    this.addCoins(groupGame, coins);
                }
                return;
            }
        }
        for (GameAlias alias : settings.getGameAliases()) {
            for (String aliasAlias : alias.getAliases()) {
                if (game.equals(aliasAlias)) {
                    for (String aliasGame : alias.getGames()) {
                        this.addCoins(aliasGame, coins);
                    }
                    return;
                }
            }
        }
        this.gameStatistics.merge(game, coins, Coins::merge);
    }

    public Stats merge(final Stats that) {
        this.tipsSent += that.tipsSent;
        this.tipsReceived += that.tipsReceived;
        this.xpSent += that.xpSent;
        this.xpReceived += that.xpReceived;
        for (Map.Entry<String, Coins> entry : this.gameStatistics.entrySet()) {
            addCoins(entry.getKey(), entry.getValue());
        }
        return this;
    }
}
