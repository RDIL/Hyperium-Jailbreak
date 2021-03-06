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

import cc.hyperium.event.client.InitializationEvent;
import cc.hyperium.event.client.PreInitializationEvent;
import cc.hyperium.event.client.GameShutDownEvent;
import cc.hyperium.event.Priority;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.EventBus;
import cc.hyperium.gui.ConfirmationPopup;
import cc.hyperium.gui.SplashProgress;
import cc.hyperium.addons.InternalAddons;
import cc.hyperium.commands.HyperiumCommandHandler;
import cc.hyperium.commands.defaults.*;
import cc.hyperium.config.DefaultConfig;
import cc.hyperium.config.Settings;
import cc.hyperium.cosmetics.HyperiumCosmetics;
import cc.hyperium.event.network.server.hypixel.minigames.MinigameListener;
import cc.hyperium.handlers.HyperiumHandlers;
import cc.hyperium.handlers.handlers.stats.PlayerStatsGui;
import cc.hyperium.mixinsimp.client.resources.HyperiumLocale;
import cc.hyperium.mods.HyperiumModIntegration;
import cc.hyperium.mods.autofriend.command.AutofriendCommand;
import cc.hyperium.mods.ToggleSprintContainer;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.StaffUtils;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.mods.CompactChat;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import com.hyperiumjailbreak.CommonChatResponder;
import java.io.File;

/**
 * The client's main class.
 *
 * @see Hyperium#INSTANCE
 */
public class Hyperium {
    /**
     * The client's mod-id String (for internal use only).
     */
    public static final String modid = "Hyperium";
    /**
     * The client's version.
     */
    public static final String version = "4.3.1";
    /**
     * The client's version as an integer.
     *
     * @see cc.hyperium.utils.VersionUtil
     */
    public static final int HYPERIUMJB_VERSION = 431;
    /**
     * The class instance.
     */
    public static final Hyperium INSTANCE = new Hyperium();
    /**
     * The global logger. Please use a per-class logger instead.
     */
    public static final Logger LOGGER = LogManager.getLogger(modid);
    /**
     * The folder for all the client's files.
     */
    public static final File folder = new File("hyperium");
    /**
     * The configuration system.
     *
     * @see DefaultConfig#register(Object)
     * @see DefaultConfig#save()
     */
    public static final DefaultConfig CONFIG = new DefaultConfig(new File(folder, "CONFIG.json"));
    private final ConfirmationPopup confirmation = new ConfirmationPopup();
    private HyperiumCosmetics cosmetics;
    private HyperiumHandlers handlers;
    private HyperiumModIntegration modIntegration;
    private boolean optifineInstalled = false;
    /**
     * If the client is running in the development environment.
     */
    public boolean isDevEnv = false;
    private boolean firstLaunch = false;

    @InvokeEvent(priority = Priority.HIGH)
    public void preInit(PreInitializationEvent event) {
        HyperiumLocale.registerHyperiumLang("en_US");
    }

    @InvokeEvent(priority = Priority.HIGH)
    public void init(InitializationEvent event) {
        LOGGER.warn("This project is NOT RUN BY THE HYPERIUM TEAM");
        LOGGER.warn("Please report bugs by DMing rdil#0001 on Discord");
        LOGGER.warn("or by emailing me@rdil.rocks");
        try {
            Multithreading.runAsync(PlayerStatsGui::new); // Don't remove
            try {
                Class.forName("net.minecraft.dispenser.BehaviorProjectileDispense");
                isDevEnv = true;
            } catch (ClassNotFoundException ignored) {
            }

            cosmetics = new HyperiumCosmetics();

            if (!Settings.FPS && Settings.THANK_WATCHDOG) {
                EventBus.INSTANCE.register(new CommonChatResponder("removed from your game for hacking", "Thanks Watchdog!", true));
            }

            // Creates the accounts dir
            firstLaunch = new File(folder.getAbsolutePath() + "/accounts").mkdirs();

            SplashProgress.setProgress(5, "Loading Handlers");
            handlers = new HyperiumHandlers();
            handlers.getGeneralChatHandler().post();

            SplashProgress.setProgress(6, "Loading Utilities");
            EventBus.INSTANCE.register(new MinigameListener());
            EventBus.INSTANCE.register(new ToggleSprintContainer());
            EventBus.INSTANCE.register(CompactChat.getInstance());
            EventBus.INSTANCE.register(confirmation);

            CONFIG.register(new ToggleSprintContainer());

            Display.setTitle("HyperiumJailbreak");

            SplashProgress.setProgress(7, "Preparing Config");
            Settings.register();
            // Register commands.
            SplashProgress.setProgress(8, "Loading Chat Commands");
            registerCommands();

            SplashProgress.setProgress(9, "Loading Mods");
            modIntegration = new HyperiumModIntegration();

            StaffUtils.clearCache();

            Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

            SplashProgress.setProgress(10, "Reloading Minecraft...");
            Minecraft.getMinecraft().refreshResources();

            SplashProgress.setProgress(11, "Finishing Up...");

            // Check if OptiFine is installed.
            try {
                Class.forName("optifine.OptiFineTweaker");
                optifineInstalled = true;
            } catch (ClassNotFoundException e) {
                optifineInstalled = false;
            }
        } catch (Throwable t) {
            Minecraft.getMinecraft().crashed(new CrashReport("Startup Failure", t));
        }
    }

    private void registerCommands() {
        HyperiumCommandHandler hyperiumCommandHandler = getHandlers().getHyperiumCommandHandler();
        hyperiumCommandHandler.registerCommand(new CommandConfigGui());
        hyperiumCommandHandler.registerCommand(new CommandClearChat());
        hyperiumCommandHandler.registerCommand(new CommandDebug());
        hyperiumCommandHandler.registerCommand(new CommandCoords());
        hyperiumCommandHandler.registerCommand(new CommandLogs());
        hyperiumCommandHandler.registerCommand(new CommandParty());
        hyperiumCommandHandler.registerCommand(new CommandGarbageCollect());
        hyperiumCommandHandler.registerCommand(new CommandMessage());
        hyperiumCommandHandler.registerCommand(new CommandDisableCommand());
        hyperiumCommandHandler.registerCommand(new AutofriendCommand());
        hyperiumCommandHandler.registerCommand(new CommandKeybinds());
    }

    private void shutdown() {
        CONFIG.save();

        // Tell the modules the game is shutting down
        EventBus.INSTANCE.post(new GameShutDownEvent());
        CONFIG.save();
    }

    /**
     * Returns the client's confirmation popup utility instance.
     *
     * @return The confirmation popup instance.
     */
    public ConfirmationPopup getConfirmation() {
        return confirmation;
    }

    /**
     * Returns the client's cosmetic manager instance.
     *
     * @return The cosmetic manager instance.
     * @see HyperiumCosmetics
     */
    public HyperiumCosmetics getCosmetics() {
        return cosmetics;
    }

    /**
     * Returns if this is the first launch of the client.
     *
     * @return If this is the first launch.
     */
    public boolean isFirstLaunch() {
        return firstLaunch;
    }

    /**
     * Returns if OptiFine is currently in the environment.
     *
     * @return If OptiFine is present.
     */
    public boolean isOptifineInstalled() {
        return optifineInstalled;
    }

    /**
     * Returns if the client is running in the development environment.
     *
     * @return If the client is running in the development environment.
     */
    public boolean isDevEnv() {
        return this.isDevEnv;
    }

    /**
     * Returns the globally usable {@link HyperiumHandlers} instance.
     *
     * @return The {@link HyperiumHandlers} instance.
     * @see HyperiumHandlers
     */
    public HyperiumHandlers getHandlers() {
        return handlers;
    }

    /**
     * Returns the client's internal addons manager.
     *
     * @deprecated This functionality has been moved to {@link HyperiumModIntegration}.
     * @return The client's internal addons manager.
     */
    @Deprecated
    public InternalAddons getInternalAddons() {
        return new InternalAddons();
    }

    /**
     * Returns the client's internal mod integration manager.
     *
     * @return The client's internal mod integration manager.
     * @see HyperiumModIntegration
     */
    public HyperiumModIntegration getModIntegration() {
        return modIntegration;
    }
}
