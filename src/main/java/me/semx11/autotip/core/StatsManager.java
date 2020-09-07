package me.semx11.autotip.core;

import com.google.gson.JsonSyntaxException;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.stats.StatsDaily;
import me.semx11.autotip.stats.StatsRange;
import me.semx11.autotip.util.FileUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StatsManager {
    private final Autotip autotip;
    private final FileUtil fileUtil;
    private final Map<LocalDate, StatsDaily> cache = new ConcurrentHashMap<>();

    private LocalDate lastDate;
    private AtomicInteger ticks;

    public StatsManager(Autotip autotip) {
        this.autotip = autotip;
        this.fileUtil = autotip.getFileUtil();
        this.lastDate = LocalDate.now();
        this.ticks = new AtomicInteger(-1);
    }

    public synchronized StatsDaily getToday() {
        return this.getToday(false);
    }

    private synchronized StatsDaily getToday(boolean readOnly) {
        LocalDate now = LocalDate.now();
        if (!lastDate.isEqual(now)) {
            this.save(this.get(lastDate));
            lastDate = now;
        }
        if (!readOnly) {
            // Save after 7 seconds (20 ticks/sec) of no access
            ticks.set(7 * 20);
        }
        return this.get(lastDate);
    }

    public StatsDaily get() {
        return this.getToday(true);
    }

    public StatsDaily get(LocalDate date) {
        if (cache.containsKey(date)) {
            return cache.get(date);
        }
        StatsDaily stats = this.load(new StatsDaily(autotip, date));
        cache.put(date, stats);
        return stats;
    }

    private StatsRange getRange(LocalDate start, LocalDate end) {
        if (start.isBefore(fileUtil.getFirstDate())) {
            start = fileUtil.getFirstDate();
        }
        if (end.isAfter(LocalDate.now())) {
            end = LocalDate.now();
        }
        StatsRange range = new StatsRange(autotip, start, end);
        // TODO: Remove stream API usage.
        List<LocalDate> dates = Stream.iterate(start, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                .collect(Collectors.toList());

        for (LocalDate date : dates) {
            range.merge(this.get(date));
        }
        return range;
    }

    public StatsRange getAll() {
        return this.getRange(fileUtil.getFirstDate(), LocalDate.now());
    }

    public void save(StatsDaily stats) {
        cache.put(stats.getDate(), stats);
        File file = stats.getFile();
        try {
            String json = autotip.getGson().toJson(stats);
            FileUtils.writeStringToFile(file, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Autotip.LOGGER.error("Could not write to " + file, e);
        }
    }

    private StatsDaily load(StatsDaily stats) {
        File file = stats.getFile();
        try {
            String json = FileUtils.readFileToString(file, "UTF-8");
            return stats.merge(autotip.getGson().fromJson(json, StatsDaily.class));
        } catch (FileNotFoundException e) {
            // Skip
            return stats;
        } catch (JsonSyntaxException | IllegalArgumentException e) {
            Autotip.LOGGER.warn(file.getName() + " has invalid contents, resetting...");
        } catch (IOException e) {
            Autotip.LOGGER.error("Could not read " + file.getName() + "!", e);
        }
        this.save(stats);
        return stats;
    }

    public void saveCycle() {
        if (ticks.get() > 0) {
            ticks.decrementAndGet();
            return;
        }
        if (ticks.get() == 0) {
            this.save(this.get(lastDate));
            ticks.decrementAndGet();
        }
    }
}
