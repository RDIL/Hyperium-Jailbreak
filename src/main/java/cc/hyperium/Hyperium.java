/*
 *     Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium;
import cc.hyperium.event.*;
import cc.hyperium.gui.ConfirmationPopup;
import cc.hyperium.gui.SplashProgress;
import cc.hyperium.gui.ColourOptions;
import cc.hyperium.addons.InternalAddons;
import cc.hyperium.commands.HyperiumCommandHandler;
import cc.hyperium.commands.defaults.*;
import cc.hyperium.config.DefaultConfig;
import cc.hyperium.config.Settings;
import cc.hyperium.cosmetics.HyperiumCosmetics;
import cc.hyperium.event.minigames.MinigameListener;
import cc.hyperium.handlers.HyperiumHandlers;
import cc.hyperium.handlers.handlers.stats.PlayerStatsGui;
import cc.hyperium.mixinsimp.client.resources.HyperiumLocale;
import cc.hyperium.mods.HyperiumModIntegration;
import cc.hyperium.mods.autofriend.command.AutofriendCommand;
import cc.hyperium.mods.autogg.AutoGG;
import cc.hyperium.mods.ToggleSprintContainer;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.GeneralStatisticsTracking;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.UniversalNetty;
import cc.hyperium.network.LoginReplyHandler;
import cc.hyperium.network.NetworkHandler;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.StaffUtils;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.mods.CompactChat;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import rocks.rdil.jailbreak.chat.CommonChatResponder;
import rocks.rdil.jailbreak.Jailbreak;
import rocks.rdil.jailbreak.BackendHandler;
import java.io.File;

public class Hyperium {
    public static final String modid = "Hyperium";
    public static final String version = "2.12.0";
    public static final Hyperium INSTANCE = new Hyperium();
    public static final Logger LOGGER = LogManager.getLogger(modid);
    public static final File folder = new File("hyperium");
    public static final DefaultConfig CONFIG = new DefaultConfig(new File(folder, "CONFIG.json"));
    private final GeneralStatisticsTracking statTrack = new GeneralStatisticsTracking();
    private final ConfirmationPopup confirmation = new ConfirmationPopup();
    private HyperiumCosmetics cosmetics;
    private HyperiumHandlers handlers;
    private HyperiumModIntegration modIntegration;
    private MinigameListener minigameListener;
    private boolean optifineInstalled = false;
    public boolean isDevEnv;
    private NetworkHandler networkHandler;
    private boolean firstLaunch = false;
    private AutoGG autogg = new AutoGG();
    private InternalAddons intAddons;
    public Jailbreak j = new Jailbreak();
    private BackendHandler bh = new BackendHandler();

    @InvokeEvent(priority = Priority.HIGH)
    public void init(InitializationEvent event) {
        HyperiumLocale.registerHyperiumLang("en_US");
        this.j.debug();
        try {
            Multithreading.runAsync(() -> {
                networkHandler = new NetworkHandler();
                CONFIG.register(networkHandler);
                new NettyClient(networkHandler);
                UniversalNetty.getInstance().getPacketManager().register(new LoginReplyHandler());
            });
            Multithreading.runAsync(() -> new PlayerStatsGui(null)); // Don't remove
            try {
                Class.forName("net.minecraft.dispenser.BehaviorProjectileDispense");
                isDevEnv = true;
            } catch (ClassNotFoundException e) {
                isDevEnv = false;
            }

            if(!Settings.FPS) {
                cosmetics = new HyperiumCosmetics();
                if (Settings.THANK_WATCHDOG && !Settings.FPS) new CommonChatResponder("removed from your game for hacking", "Thanks Watchdog!", true);
            }

            // Creates the accounts dir
            firstLaunch = new File(folder.getAbsolutePath() + "/accounts").mkdirs();

            SplashProgress.setProgress(5, "Loading Handlers");
            EventBus.INSTANCE.register(autogg);
            handlers = new HyperiumHandlers();
            handlers.getGeneralChatHandler().post();

            SplashProgress.setProgress(6, "Loading Utilities");
            minigameListener = new MinigameListener();
            EventBus.INSTANCE.register(minigameListener);
            EventBus.INSTANCE.register(new ToggleSprintContainer());
            EventBus.INSTANCE.register(CompactChat.getInstance());
            EventBus.INSTANCE.register(confirmation);

            // Register statistics tracking.
            EventBus.INSTANCE.register(statTrack);
            CONFIG.register(statTrack);
            CONFIG.register(new ToggleSprintContainer());

            SplashProgress.setProgress(7, "Starting");
            Display.setTitle("HyperiumJailbreak");

            SplashProgress.setProgress(9, "Preparing Config");
            Settings.register();
            Hyperium.CONFIG.register(new ColourOptions());
            // Register commands.
            SplashProgress.setProgress(10, "Loading Chat Commands");
            registerCommands();
            EventBus.INSTANCE.register(PurchaseApi.getInstance());

            SplashProgress.setProgress(11, "Loading Mods");
            modIntegration = new HyperiumModIntegration();
            this.intAddons = new InternalAddons();

            StaffUtils.clearCache();

            Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

            SplashProgress.setProgress(12, "Reloading Jailbreak Manager");
            Minecraft.getMinecraft().refreshResources();

            SplashProgress.setProgress(13, "Finishing Up...");

            // Check if OptiFine is installed.
            try {
                Class.forName("optifine.OptiFineTweaker");
                optifineInstalled = true;
            } catch (ClassNotFoundException e) {
                optifineInstalled = false;
            }
            // update player count
            this.bh.apiRequest("join");
        } catch (Throwable t) {
            Minecraft.getMinecraft().crashed(new CrashReport("Startup Failure", t));
        }
    }

    private void registerCommands() {
        HyperiumCommandHandler hyperiumCommandHandler = getHandlers().getHyperiumCommandHandler();
        hyperiumCommandHandler.registerCommand(new CommandConfigGui());
        hyperiumCommandHandler.registerCommand(new CommandClearChat());
        hyperiumCommandHandler.registerCommand(new CommandNameHistory());
        hyperiumCommandHandler.registerCommand(new CommandDebug());
        hyperiumCommandHandler.registerCommand(new CommandCoords());
        hyperiumCommandHandler.registerCommand(new CommandLogs());
        hyperiumCommandHandler.registerCommand(new CommandPing());
        hyperiumCommandHandler.registerCommand(new CommandStats());
        hyperiumCommandHandler.registerCommand(new CommandParty());
        hyperiumCommandHandler.registerCommand(new CommandGarbageCollect());
        hyperiumCommandHandler.registerCommand(new CommandMessage());
        hyperiumCommandHandler.registerCommand(new CommandDisableCommand());
        if(!Settings.FPS) {
            hyperiumCommandHandler.registerCommand(new CustomLevelheadCommand());
            hyperiumCommandHandler.registerCommand(new AutofriendCommand());
            hyperiumCommandHandler.registerCommand(new CommandStatistics());
        }
        hyperiumCommandHandler.registerCommand(new CommandGuild());
        hyperiumCommandHandler.registerCommand(new CommandKeybinds());
    }

    private void shutdown() {
        CONFIG.save();

        // Remove from online players
        this.bh.apiRequest("leave");

        // Tell the modules the game is shutting down
        EventBus.INSTANCE.post(new GameShutDownEvent());
    }

    public ConfirmationPopup getConfirmation() {
        return confirmation;
    }

    public HyperiumCosmetics getCosmetics() {
        return cosmetics;
    }

    public NetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    public boolean isFirstLaunch() {
        return firstLaunch;
    }

    public boolean isOptifineInstalled() {
        return optifineInstalled;
    }

    public MinigameListener getMinigameListener() {
        return minigameListener;
    }

    public boolean isDevEnv() {
        return this.isDevEnv;
    }

    public HyperiumHandlers getHandlers() {
        return handlers;
    }

    public InternalAddons getInternalAddons() {
        return intAddons;
    }

    public HyperiumModIntegration getModIntegration() {
        return modIntegration;
    }

    private static void noop() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @InvokeEvent
    public void worldSwap(ServerJoinEvent event) {
        Multithreading.runAsync(() -> {
            while (Minecraft.getMinecraft().thePlayer == null) {
                noop();
            }
            if (Hyperium.INSTANCE.bh.apiUpdateCheck()) {
                try {
                    Thread.sleep(2000);
                    getHandlers().getGeneralChatHandler().sendMessage(ChatColor.RED + "An update for the client is now available at " + ChatColor.WHITE + "https://rdil.rocks/update", true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
