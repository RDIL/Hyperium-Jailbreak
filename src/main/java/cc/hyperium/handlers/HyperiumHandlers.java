/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
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

package cc.hyperium.handlers;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.HyperiumCommandHandler;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.client.TickEvent;
import cc.hyperium.gui.ScoreboardRenderer;
import cc.hyperium.handlers.handlers.*;
import cc.hyperium.handlers.handlers.animation.cape.CapeHandler;
import cc.hyperium.handlers.handlers.chat.*;
import cc.hyperium.handlers.handlers.data.HypixelAPI;
import cc.hyperium.handlers.handlers.hud.VanillaEnhancementsHud;
import cc.hyperium.handlers.handlers.keybinds.KeyBindHandler;
import cc.hyperium.handlers.handlers.reach.ReachDisplay;
import cc.hyperium.handlers.handlers.stats.StatsHandler;
import cc.hyperium.mods.PerspectiveModifierHandler;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.List;

public class HyperiumHandlers {
    private LocationHandler locationHandler;
    private HypixelDetector hypixelDetector;
    private CommandQueue commandQueue;
    private CapeHandler capeHandler;
    private List<HyperiumChatHandler> chatHandlers;
    private GeneralChatHandler generalChatHandler;
    private HypixelAPI dataHandler;
    private GuiDisplayHandler guiDisplayHandler;
    private KeyBindHandler keybindHandler;
    private HyperiumCommandHandler commandHandler;
    private ScoreboardRenderer scoreboardRenderer;
    private OtherConfigOptions configOptions;
    private FlipHandler flipHandler;
    private PerspectiveModifierHandler perspectiveHandler;
    private StatsHandler statsHandler;
    private SettingsHandler settingsHandler;

    public HyperiumHandlers() {
        settingsHandler = new SettingsHandler();
        chatHandlers = new ArrayList<>();
        register(configOptions = new OtherConfigOptions());
        register(FontRendererData.INSTANCE);
        register(generalChatHandler = new GeneralChatHandler(chatHandlers));
        register(perspectiveHandler = new PerspectiveModifierHandler());
        register(keybindHandler = new KeyBindHandler());
        register(hypixelDetector = new HypixelDetector());
        register(flipHandler = new FlipHandler());
        register(new ReachDisplay());
        register(locationHandler = new LocationHandler());
        register(new VanillaEnhancementsHud());
        register(new ResolutionUtil());
        register(capeHandler = new CapeHandler());
        register(guiDisplayHandler = new GuiDisplayHandler());
        register(scoreboardRenderer = new ScoreboardRenderer());
        register(statsHandler = new StatsHandler());
        register(new BroadcastEvents());
        commandQueue = new CommandQueue();
        dataHandler = new HypixelAPI();
        registerChatHandler(new DMChatHandler());
        registerChatHandler(new FriendRequestChatHandler());
        registerChatHandler(new PartyInviteChatHandler());
        registerChatHandler(new GuildChatHandler());
        EventBus.INSTANCE.register(this);
        register(commandHandler = new HyperiumCommandHandler());
    }

    public StatsHandler getStatsHandler() {
        return statsHandler;
    }

    public HyperiumCommandHandler getCommandHandler() {
        return commandHandler;
    }

    public FlipHandler getFlipHandler() {
        return flipHandler;
    }

    public SettingsHandler getSettingsHandler() {
        return settingsHandler;
    }

    private void registerChatHandler(HyperiumChatHandler chatHandler) {
        register(chatHandler);
        chatHandlers.add(chatHandler);
    }

    @SuppressWarnings("unused")
    @InvokeEvent
    public void tick(TickEvent event) {
        // Runs first tick
        if (Minecraft.getMinecraft().getIntegratedServer() == null || Minecraft.getMinecraft().getIntegratedServer().getCommandManager() == null) return;
        EventBus.INSTANCE.unregister(HyperiumHandlers.class);
    }

    private void register(Object object) {
        Hyperium.CONFIG.register(object);
        EventBus.INSTANCE.register(object);
    }

    public LocationHandler getLocationHandler() {
        return locationHandler;
    }

    public HypixelDetector getHypixelDetector() {
        return hypixelDetector;
    }

    public CommandQueue getCommandQueue() {
        return commandQueue;
    }

    public GeneralChatHandler getGeneralChatHandler() {
        return generalChatHandler;
    }

    public HypixelAPI getDataHandler() {
        return dataHandler;
    }

    public GuiDisplayHandler getGuiDisplayHandler() {
        return guiDisplayHandler;
    }

    public KeyBindHandler getKeybindHandler() {
        return keybindHandler;
    }

    public HyperiumCommandHandler getHyperiumCommandHandler() {
        return commandHandler;
    }

    public ScoreboardRenderer getScoreboardRenderer() {
        return scoreboardRenderer;
    }

    public OtherConfigOptions getConfigOptions() {
        return configOptions;
    }

    public CapeHandler getCapeHandler() {
        return capeHandler;
    }

    public PerspectiveModifierHandler getPerspectiveHandler() {
        return perspectiveHandler;
    }
}
