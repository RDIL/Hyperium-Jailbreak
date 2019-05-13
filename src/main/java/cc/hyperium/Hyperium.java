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
import cc.hyperium.addons.InternalAddons;
import cc.hyperium.commands.HyperiumCommandHandler;
import cc.hyperium.commands.defaults.*;
import cc.hyperium.config.DefaultConfig;
import cc.hyperium.config.Settings;
import cc.hyperium.cosmetics.HyperiumCosmetics;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.GameShutDownEvent;
import cc.hyperium.event.InitializationEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.Priority;
import cc.hyperium.event.minigames.MinigameListener;
import cc.hyperium.gui.*;
import cc.hyperium.handlers.HyperiumHandlers;
import cc.hyperium.handlers.handlers.stats.PlayerStatsGui;
import cc.hyperium.installer.utils.http.NameValuePair;
import cc.hyperium.installer.utils.http.client.HttpClient;
import cc.hyperium.installer.utils.http.client.entity.UrlEncodedFormEntity;
import cc.hyperium.installer.utils.http.client.methods.HttpPost;
import cc.hyperium.installer.utils.http.impl.client.HttpClients;
import cc.hyperium.installer.utils.http.message.BasicNameValuePair;
import cc.hyperium.mixinsimp.client.resources.HyperiumLocale;
import cc.hyperium.mixinsimp.renderer.FontFixValues;
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
import cc.hyperium.utils.mods.CompactChat;
import jb.Metadata;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import rocks.rdil.jailbreak.chat.CommonChatResponder;
import rocks.rdil.jailbreak.Jailbreak;
import java.io.*;
import java.util.*;

public class Hyperium {
    public static final Hyperium INSTANCE = new Hyperium();
    public static final Logger LOGGER = LogManager.getLogger(Metadata.getModid());
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
    public Jailbreak j = new Jailbreak();

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
            handlers.postInit();

            SplashProgress.setProgress(6, "Registering Utilities");
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
            new InternalAddons();

            Multithreading.runAsync(() -> StaffUtils.clearCache());

            Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

            SplashProgress.setProgress(12, "Reloading Jailbreak Manager");
            Minecraft.getMinecraft().refreshResources();

            SplashProgress.setProgress(13, "Almost Done, Finishing Up");
            if (FontFixValues.INSTANCE == null) FontFixValues.INSTANCE = new FontFixValues();
            Multithreading.runAsync(() -> EventBus.INSTANCE.register(FontFixValues.INSTANCE));

            // Check if OptiFine is installed.
            try {
                Class.forName("optifine.OptiFineTweaker");
                optifineInstalled = true;
            } catch (ClassNotFoundException e) {
                optifineInstalled = false;
            }
            // update player count
            try {
                HttpClient httpclient = HttpClients.createDefault();
                HttpPost httppost = new HttpPost("http://backend.rdil.rocks/join");

                // Request parameters and other properties.
                List<NameValuePair> params = new ArrayList<NameValuePair>(1);
                params.add(new BasicNameValuePair("uuid", "123123"));
                try {
                    httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // Execute and get the response.
                httpclient.execute(httppost);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Throwable t) {
            Minecraft.getMinecraft().crashed(new CrashReport("Hyperium Startup Failure", t));
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
        hyperiumCommandHandler.registerCommand(new CommandResize());
        hyperiumCommandHandler.registerCommand(new CommandGarbageCollect());
        hyperiumCommandHandler.registerCommand(new CommandMessage());
        hyperiumCommandHandler.registerCommand(new CommandDisableCommand());
        if(!Settings.FPS) {
            hyperiumCommandHandler.registerCommand(new CustomLevelheadCommand());
            hyperiumCommandHandler.registerCommand(new AutofriendCommand());
            hyperiumCommandHandler.registerCommand(new CommandStatistics());
            hyperiumCommandHandler.registerCommand(new CommandQuests());
        }
        hyperiumCommandHandler.registerCommand(new CommandGuild());
        hyperiumCommandHandler.registerCommand(new CommandKeybinds());
    }

    private void shutdown() {
        CONFIG.save();

        // Remove from online players
        try{
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost("http://backend.rdil.rocks/leave");

            // Request parameters and other properties.
            List<NameValuePair> params = new ArrayList<NameValuePair>(0);
            try {
                httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                //Execute and get the response.
                httpclient.execute(httppost);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Minecraft.getMinecraft().crashed(new CrashReport("Hyperium Shutdown Failure", e));
        }

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

    public Jailbreak getJailbreak() {
        return j;
    }

    public HyperiumHandlers getHandlers() {
        return handlers;
    }

    public HyperiumModIntegration getModIntegration() {
        return modIntegration;
    }
}
