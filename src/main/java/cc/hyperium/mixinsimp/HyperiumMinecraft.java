package cc.hyperium.mixinsimp;

import cc.hyperium.Hyperium;
import cc.hyperium.config.provider.IntegrationOptionsProvider;
import cc.hyperium.gui.SplashProgress;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.gui.GuiOpenEvent;
import cc.hyperium.event.interact.KeypressEvent;
import cc.hyperium.event.interact.KeyreleaseEvent;
import cc.hyperium.event.interact.LeftMouseClickEvent;
import cc.hyperium.event.interact.MouseButtonEvent;
import cc.hyperium.event.client.PreInitializationEvent;
import cc.hyperium.event.render.RenderPlayerEvent;
import cc.hyperium.event.interact.RightMouseClickEvent;
import cc.hyperium.event.network.server.SingleplayerJoinEvent;
import cc.hyperium.event.client.TickEvent;
import cc.hyperium.event.world.WorldChangeEvent;
import cc.hyperium.event.world.WorldUnloadEvent;
import cc.hyperium.gui.GuiHyperiumScreenMainMenu;
import cc.hyperium.internal.addons.AddonBootstrap;
import cc.hyperium.internal.addons.AddonMinecraftBootstrap;
import cc.hyperium.internal.addons.IAddon;
import cc.hyperium.mixins.IMixinMinecraft;
import cc.hyperium.utils.AddonWorkspaceResourcePack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.Timer;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.io.File;
import java.util.List;

public class HyperiumMinecraft {
    private Minecraft parent;
    public HyperiumMinecraft(Minecraft parent) {
        this.parent = parent;
    }

    public void preinit(List<IResourcePack> defaultResourcePacks, DefaultResourcePack mcDefaultResourcePack) {
        EventBus.INSTANCE.register(Hyperium.INSTANCE);

        defaultResourcePacks.add(mcDefaultResourcePack);
        for (File file : AddonBootstrap.INSTANCE.getAddonResourcePacks()) {
            defaultResourcePacks.add(file == null ? new AddonWorkspaceResourcePack() : new FileResourcePack(file));
        }
        AddonMinecraftBootstrap.INSTANCE.init();
        EventBus.INSTANCE.post(new PreInitializationEvent());
    }

    public void loop(boolean inGameHasFocus, WorldClient theWorld, EntityPlayerSP thePlayer, RenderManager renderManager, Timer timer) {
        if (inGameHasFocus && theWorld != null) {
            new RenderPlayerEvent(thePlayer, renderManager, renderManager.viewerPosZ, renderManager.viewerPosY, renderManager.viewerPosZ,timer.renderPartialTicks);
        }
    }

    @SuppressWarnings("deprecation")
    public void startGame() {
        EventBus.INSTANCE.post(new cc.hyperium.event.client.InitializationEvent());
        EventBus.INSTANCE.post(new cc.hyperium.event.InitializationEvent());
    }

    public void runTick(Profiler mcProfiler) {
        mcProfiler.startSection("hyperium_tick");
        EventBus.INSTANCE.post(new TickEvent());
        mcProfiler.endSection();
    }

    public void runTickKeyboard() {
        int key = Keyboard.getEventKey();
        boolean repeat = Keyboard.isRepeatEvent();
        boolean press = Keyboard.getEventKeyState();

        if (press) {
            // Key pressed.
            EventBus.INSTANCE.post(new KeypressEvent(key, repeat));
        } else {
            // Key released.
            EventBus.INSTANCE.post(new KeyreleaseEvent(key, repeat));
        }
    }

    public void clickMouse() {
        EventBus.INSTANCE.post(new LeftMouseClickEvent());
    }

    public void rightClickMouse() {
        EventBus.INSTANCE.post(new RightMouseClickEvent());
    }

    public void launchIntegratedServer() {
        EventBus.INSTANCE.post(new SingleplayerJoinEvent());
    }

    public void displayFix(CallbackInfo ci, boolean fullscreen, int displayWidth, int displayHeight) throws LWJGLException {
        Display.setFullscreen(false);
        if (fullscreen) {
            if (IntegrationOptionsProvider.WINDOWED_FULLSCREEN) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
            } else {
                Display.setFullscreen(true);
                DisplayMode displaymode = Display.getDisplayMode();
                ((IMixinMinecraft) parent).setDisplayWidth(Math.max(1, displaymode.getWidth()));
                ((IMixinMinecraft) parent).setDisplayHeight(Math.max(1, displaymode.getHeight()));
            }
        } else {
            if (IntegrationOptionsProvider.WINDOWED_FULLSCREEN) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
            } else {
                Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
            }
        }
        Display.setResizable(false);
        Display.setResizable(true);

        // effectively overwrites the method
        ci.cancel();
    }

    public void fullScreenFix(boolean fullscreen, int displayWidth, int displayHeight) throws LWJGLException {
        if (IntegrationOptionsProvider.WINDOWED_FULLSCREEN) {
            if (fullscreen) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
                Display.setDisplayMode(Display.getDesktopDisplayMode());
                Display.setLocation(0, 0);
                Display.setFullscreen(false);
            } else {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
                Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
            }
        } else {
            Display.setFullscreen(fullscreen);
            System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
        }
        Display.setResizable(false);
        Display.setResizable(true);
    }

    public void displayGuiScreen(GuiScreen guiScreenIn, GuiScreen currentScreen,
                                 WorldClient theWorld, EntityPlayerSP thePlayer, GameSettings gameSettings, GuiIngame ingameGUI) {
        if (currentScreen != null) currentScreen.onGuiClosed();

        if (guiScreenIn == null && theWorld == null) {
            guiScreenIn = new GuiHyperiumScreenMainMenu();
        } else if (guiScreenIn == null && thePlayer.getHealth() <= 0.0F) {
            guiScreenIn = new GuiGameOver();
        }

        GuiOpenEvent event = new GuiOpenEvent(guiScreenIn);
        EventBus.INSTANCE.post(event);

        if (event.isCancelled()) return;

        guiScreenIn = event.getGui();
        if (currentScreen != null && guiScreenIn != currentScreen) currentScreen.onGuiClosed();
        if (currentScreen != null) EventBus.INSTANCE.unregister(currentScreen);

        if (guiScreenIn instanceof GuiHyperiumScreenMainMenu) gameSettings.showDebugInfo = false;

        ((IMixinMinecraft) parent).setCurrentScreen(guiScreenIn);

        if (guiScreenIn != null) {
            parent.setIngameNotInFocus();
            ScaledResolution scaledresolution = new ScaledResolution(parent);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            guiScreenIn.setWorldAndResolution(parent, i, j);
            parent.skipRenderWorld = false;
        } else {
            parent.getSoundHandler().resumeSounds();
            parent.setIngameFocus();
        }

        if (Hyperium.INSTANCE.getHandlers() != null) Hyperium.INSTANCE.getHandlers().getKeybindHandler().releaseAllKeybinds();
    }

    public void onStartGame() {
        SplashProgress.setProgress(1, "Starting Game...");
    }

    public void loadWorld() {
        if (Minecraft.getMinecraft().theWorld != null) new WorldUnloadEvent().post();

        EventBus.INSTANCE.post(new WorldChangeEvent());
    }

    public void runTickMouseButton() {
        // Activates for EVERY mouse button.
        int i = Mouse.getEventButton();
        boolean state = Mouse.getEventButtonState();
        if (state) {
            EventBus.INSTANCE.post(new MouseButtonEvent(i, true));
        } else {
            EventBus.INSTANCE.post(new MouseButtonEvent(i, false));
        }
    }

    public void shutdown() {
        for (IAddon addon : AddonMinecraftBootstrap.INSTANCE.getLoadedAddons()) {
            addon.onClose();
        }
    }
}
