package me.semx11.autotip.message;

import java.util.regex.Pattern;
import me.semx11.autotip.stats.StatsDaily;

class HoverMessageMatcher extends MessageMatcher {
    private final StatsType statsType;

    HoverMessageMatcher(Pattern pattern, String input, StatsType statsType) {
        super(pattern, input);
        this.statsType = statsType;
    }

    void applyStats(StatsDaily stats) {
        statsType.getConsumer().accept(stats, this);
    }
}
