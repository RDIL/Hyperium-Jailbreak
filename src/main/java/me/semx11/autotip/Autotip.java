package me.semx11.autotip;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import com.hyperiumjailbreak.ChatCanceller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.GameProfile;
import me.semx11.autotip.api.RequestHandler;
import me.semx11.autotip.api.reply.impl.LocaleReply;
import me.semx11.autotip.api.reply.impl.SettingsReply;
import me.semx11.autotip.api.request.impl.LocaleRequest;
import me.semx11.autotip.api.request.impl.SettingsRequest;
import me.semx11.autotip.chat.LocaleHolder;
import me.semx11.autotip.chat.MessageUtil;
import me.semx11.autotip.command.CommandAbstract;
import me.semx11.autotip.command.impl.CommandAutotip;
import me.semx11.autotip.command.impl.CommandLimbo;
import me.semx11.autotip.config.Config;
import me.semx11.autotip.config.GlobalSettings;
import me.semx11.autotip.core.SessionManager;
import me.semx11.autotip.core.StatsManager;
import me.semx11.autotip.core.TaskManager;
import me.semx11.autotip.event.Event;
import me.semx11.autotip.event.impl.EventChatReceived;
import me.semx11.autotip.event.impl.EventClientConnection;
import me.semx11.autotip.event.impl.EventClientTick;
import me.semx11.autotip.gson.creator.ConfigCreator;
import me.semx11.autotip.gson.creator.StatsDailyCreator;
import me.semx11.autotip.gson.exclusion.AnnotationExclusionStrategy;
import me.semx11.autotip.stats.StatsDaily;
import me.semx11.autotip.universal.UniversalUtil;
import me.semx11.autotip.util.FileUtil;
import me.semx11.autotip.util.MinecraftVersion;
import me.semx11.autotip.util.Version;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class Autotip {
    public static final Logger LOGGER = Hyperium.LOGGER;
    private static final String VERSION = "3.0";
    public static IChatComponent tabHeader;
    private final List<Event> events = new ArrayList<>();
    private final List<CommandAbstract> commands = new ArrayList<>();
    private boolean initialized = false;
    private Minecraft minecraft;
    private MinecraftVersion mcVersion;
    private Version version;
    private Gson gson;

    private FileUtil fileUtil;
    private MessageUtil messageUtil;

    private Config config;
    private GlobalSettings globalSettings;
    private LocaleHolder localeHolder;

    private TaskManager taskManager;
    private SessionManager sessionManager;
    private StatsManager statsManager;

    public boolean isInitialized() {
        return initialized;
    }

    public Minecraft getMinecraft() {
        return minecraft;
    }

    public GameProfile getGameProfile() {
        return minecraft.getSession().getProfile();
    }

    public MinecraftVersion getMcVersion() {
        return mcVersion;
    }

    public Version getVersion() {
        return version;
    }

    public Gson getGson() {
        return gson;
    }

    public FileUtil getFileUtil() {
        return fileUtil;
    }

    public MessageUtil getMessageUtil() {
        return messageUtil;
    }

    public Config getConfig() {
        return config;
    }

    public GlobalSettings getGlobalSettings() {
        return globalSettings;
    }

    public LocaleHolder getLocaleHolder() {
        return localeHolder;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }

    public void init() {
        EventBus.INSTANCE.register(new ChatCanceller("already tipped"));
        RequestHandler.setAutotip(this);
        UniversalUtil.setAutotip(this);
        this.minecraft = Minecraft.getMinecraft();
        this.mcVersion = UniversalUtil.getMinecraftVersion();
        this.version = new Version(VERSION);

        this.messageUtil = new MessageUtil(this);
        this.registerEvents(new EventClientTick(this));

        try {
            this.fileUtil = new FileUtil(this);
            this.gson = new GsonBuilder()
                    .registerTypeAdapter(Config.class, new ConfigCreator(this))
                    .registerTypeAdapter(StatsDaily.class, new StatsDailyCreator(this))
                    .setExclusionStrategies(new AnnotationExclusionStrategy())
                    .setPrettyPrinting()
                    .create();

            this.config = new Config(this);
            this.reloadGlobalSettings();
            this.reloadLocale();

            this.taskManager = new TaskManager();
            this.sessionManager = new SessionManager(this);
            this.statsManager = new StatsManager(this);

            this.fileUtil.createDirectories();
            this.config.load();

            this.registerEvents(
                    new EventClientConnection(this),
                    new EventChatReceived(this)
            );
            this.registerCommands(
                    new CommandAutotip(this),
                    new CommandLimbo(this)
            );
            Runtime.getRuntime().addShutdownHook(new Thread(sessionManager::logout));
            this.initialized = true;
        } catch (IOException | IllegalStateException e) {
            messageUtil.send("Autotip is disabled.");
        }
    }

    public void reloadGlobalSettings() {
        SettingsReply reply = SettingsRequest.of(this).execute();
        if (!reply.isSuccess()) {
            throw new IllegalStateException("Connection error while fetching global settings");
        }
        this.globalSettings = reply.getSettings();
    }

    public void reloadLocale() {
        LocaleReply reply = LocaleRequest.of(this).execute();
        if (!reply.isSuccess()) {
            throw new IllegalStateException("Could not fetch locale");
        }
        this.localeHolder = reply.getLocaleHolder();
    }

    public <T extends Event> T getEvent(Class<T> clazz) {
        return (T) events.stream()
                .filter(event -> event.getClass().equals(clazz))
                .findFirst().orElse(null);
    }

    public <T extends CommandAbstract> T getCommand(Class<T> clazz) {
        return (T) commands.stream()
                .filter(command -> command.getClass().equals(clazz))
                .findFirst().orElse(null);
    }

    private void registerEvents(Event... events) {
        for (Event event : events) {
            EventBus.INSTANCE.register(event);
            this.events.add(event);
        }
    }

    private void registerCommands(CommandAbstract... commands) {
        for (CommandAbstract command : commands) {
            Hyperium.INSTANCE.getHandlers().getCommandHandler().registerCommand(command);
            this.commands.add(command);
        }
    }
}
