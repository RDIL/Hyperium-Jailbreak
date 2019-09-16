package me.semx11.autotip.stats;

import java.io.File;
import java.time.LocalDate;
import me.semx11.autotip.Autotip;

public class StatsDaily extends Stats {
    protected LocalDate date;

    public StatsDaily(Autotip autotip) {
        super(autotip);
    }

    public StatsDaily(Autotip autotip, LocalDate date) {
        super(autotip);
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public File getFile() {
        return autotip.getFileUtil().getStatsFile(date);
    }

    @Override
    public StatsDaily merge(final Stats that) {
        if (!(that instanceof StatsDaily)) {
            throw new IllegalArgumentException("Cannot merge with StatsDaily");
        }
        StatsDaily stats = (StatsDaily) that;
        if (!date.isEqual(stats.date)) {
            throw new IllegalArgumentException("Dates do not match!");
        }
        super.merge(that);
        return this;
    }

    public void save() {
        autotip.getStatsManager().save(this);
    }
}
