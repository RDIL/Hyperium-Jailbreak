package me.semx11.autotip.config;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import me.semx11.autotip.message.Message;
import me.semx11.autotip.message.StatsMessage;

public class GlobalSettings {
    private String hypixelHeader;
    private int xpPerTipSent;
    private int xpPerTipReceived;
    private List<GameGroup> gameGroups;
    private List<GameAlias> gameAliases;
    private List<Message> messages;
    private List<StatsMessage> statsMessages;

    public String getHypixelHeader() {
        return hypixelHeader;
    }

    public int getXpPerTipSent() {
        return xpPerTipSent;
    }

    public int getXpPerTipReceived() {
        return xpPerTipReceived;
    }

    public List<GameGroup> getGameGroups() {
        return gameGroups;
    }

    public List<GameAlias> getGameAliases() {
        return gameAliases;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<StatsMessage> getStatsMessages() {
        return statsMessages;
    }

    public static class GameGroup {
        private String name;
        private Set<String> games;

        public String getName() {
            return name;
        }

        public Set<String> getGames() {
            return games;
        }
    }

    public static class GameAlias {
        private String alias;
        private List<String> aliases;

        private String game;
        private List<String> games;

        public List<String> getAliases() {
            if (alias != null) {
                return Collections.singletonList(alias);
            }
            return aliases;
        }

        public List<String> getGames() {
            if (game != null) {
                return Collections.singletonList(game);
            }
            return games;
        }
    }
}
