package me.semx11.autotip.util;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import me.semx11.autotip.Autotip;
import org.apache.commons.io.FilenameUtils;

public class FileUtil {
    private final Path userDir;
    private final Path statsDir;

    private LocalDate firstDate;

    public FileUtil(Autotip autotip) {
        this.userDir = this.getRawPath("mods/autotip/" + autotip.getGameProfile().getId());
        this.statsDir = this.getPath("stats");
    }

    public void createDirectories() throws IOException {
        if (!Files.exists(statsDir)) {
            Files.createDirectories(statsDir);
        }
    }

    private Path getStatsDir() {
        return statsDir;
    }

    public boolean exists(String path) {
        return Files.exists(this.getPath(path));
    }

    public File getStatsFile(LocalDate localDate) {
        return this.getFile(this.statsDir, localDate.format(ISO_LOCAL_DATE) + ".at");
    }

    public LocalDate getFirstDate() {
        if (firstDate != null) {
            return firstDate;
        }
        try {
            return firstDate = Files.list(this.getStatsDir())
                    .map(this::getDateFromPath)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElseGet(LocalDate::now);
        } catch (IOException e) {
            return LocalDate.now();
        }
    }

    private LocalDate getDateFromPath(Path path) {
        String name = FilenameUtils.getBaseName(path.getFileName().toString());
        try {
            return LocalDate.parse(name);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public File getFile(String path) {
        return this.getPath(path).toFile();
    }

    private Path getPath(String path) {
        return this.getPath(this.userDir, path);
    }

    private File getFile(Path directory, String path) {
        return this.getPath(directory, path).toFile();
    }

    private Path getPath(Path directory, String path) {
        return directory.resolve(this.separator(path));
    }

    private Path getRawPath(String path) {
        return Paths.get(this.separator(path));
    }

    private String separator(String s) {
        return s.replaceAll("///", File.separator);
    }
}
