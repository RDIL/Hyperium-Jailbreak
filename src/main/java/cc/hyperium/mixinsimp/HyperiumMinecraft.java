package cc.hyperium.mixinsimp;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.GuiOpenEvent;
import cc.hyperium.event.InitializationEvent;
import cc.hyperium.event.KeypressEvent;
import cc.hyperium.event.KeyreleaseEvent;
import cc.hyperium.event.LeftMouseClickEvent;
import cc.hyperium.event.MouseButtonEvent;
import cc.hyperium.event.PreInitializationEvent;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.event.RightMouseClickEvent;
import cc.hyperium.event.SingleplayerJoinEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.event.WorldChangeEvent;
import cc.hyperium.event.WorldUnloadEvent;
import cc.hyperium.gui.CrashReportGUI;
import cc.hyperium.gui.GuiHyperiumScreenMainMenu;
import cc.hyperium.internal.addons.AddonBootstrap;
import cc.hyperium.internal.addons.AddonMinecraftBootstrap;
import cc.hyperium.internal.addons.IAddon;
import cc.hyperium.mixins.IMixinMinecraft;
import cc.hyperium.utils.AddonWorkspaceResourcePack;
import cc.hyperium.utils.Utils;
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
import net.minecraft.crash.CrashReport;
import net.minecraft.init.Bootstrap;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.Timer;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import rocks.rdil.jailbreak.util.OS;

public class HyperiumMinecraft {
    private Minecraft parent;
    public HyperiumMinecraft(Minecraft parent) {
        this.parent = parent;
    }

    public void preinit(List<IResourcePack> defaultResourcePacks, DefaultResourcePack mcDefaultResourcePack) {
        EventBus.INSTANCE.register(Hyperium.INSTANCE);

        defaultResourcePacks.add(mcDefaultResourcePack);
        for (File file : AddonBootstrap.getAddonResourcePacks()) {
            defaultResourcePacks.add(file == null ? new AddonWorkspaceResourcePack() : new FileResourcePack(file));
        }
        AddonMinecraftBootstrap.init();
        EventBus.INSTANCE.post(new PreInitializationEvent());
    }

    public void loop(boolean inGameHasFocus, WorldClient theWorld, EntityPlayerSP thePlayer, RenderManager renderManager, Timer timer) {
        if (inGameHasFocus && theWorld != null) {
            RenderPlayerEvent event = new RenderPlayerEvent(thePlayer, renderManager, renderManager.viewerPosZ, renderManager.viewerPosY, renderManager.viewerPosZ,
                timer.renderPartialTicks);
        }
    }

    public void startGame() {
        EventBus.INSTANCE.post(new InitializationEvent());
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
            if (Settings.WINDOWED_FULLSCREEN) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
            } else {
                Display.setFullscreen(true);
                DisplayMode displaymode = Display.getDisplayMode();
                ((IMixinMinecraft) parent).setDisplayWidth(Math.max(1, displaymode.getWidth()));
                ((IMixinMinecraft) parent).setDisplayHeight(Math.max(1, displaymode.getHeight()));
            }
        } else {
            if (Settings.WINDOWED_FULLSCREEN) {
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
        if (Settings.WINDOWED_FULLSCREEN) {
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

    public void setWindowIcon() {
        if (!OS.isMacintosh()) {
            try (InputStream inputStream16x = Minecraft.class
                .getResourceAsStream("/assets/hyperium/icons/icon-16x.png");
                 InputStream inputStream32x = Minecraft.class
                     .getResourceAsStream("/assets/hyperium/icons/icon-32x.png")) {
                ByteBuffer[] icons = new ByteBuffer[]{
                    Utils.INSTANCE.readImageToBuffer(inputStream16x),
                    Utils.INSTANCE.readImageToBuffer(inputStream32x)};
                Display.setIcon(icons);
            } catch (Exception e) {
                Hyperium.LOGGER.error("Couldn't set Icon. Error:", e);
            }
        }
    }

    public void displayGuiScreen(GuiScreen guiScreenIn, GuiScreen currentScreen,
                                 WorldClient theWorld, EntityPlayerSP thePlayer, GameSettings gameSettings, GuiIngame ingameGUI) {
        if (currentScreen != null) currentScreen.onGuiClosed();

        if (guiScreenIn == null && theWorld == null) {
            guiScreenIn = new GuiHyperiumScreenMainMenu();
        } else if (guiScreenIn == null && thePlayer.getHealth() <= 0.0F) {
            guiScreenIn = new GuiGameOver();
        }

        GuiScreen old = currentScreen;
        GuiOpenEvent event = new GuiOpenEvent(guiScreenIn);
        EventBus.INSTANCE.post(event);

        if (event.isCancelled()) return;

        guiScreenIn = event.getGui();
        if (old != null && guiScreenIn != old) old.onGuiClosed();
        if (old != null) EventBus.INSTANCE.unregister(old);

        if (guiScreenIn instanceof GuiHyperiumScreenMainMenu) {
            gameSettings.showDebugInfo = false;
        }

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

    public void loadWorld() {
        if (Minecraft.getMinecraft().theWorld != null) new WorldUnloadEvent().post();

        EventBus.INSTANCE.post(new WorldChangeEvent());
    }

    public void runTickMouseButton(CallbackInfo ci) {
        // Activates for EVERY mouse button.
        int i = Mouse.getEventButton();
        boolean state = Mouse.getEventButtonState();
        if (state) {
            EventBus.INSTANCE.post(new MouseButtonEvent(i, true));
        } else {
            EventBus.INSTANCE.post(new MouseButtonEvent(i, false));
        }
    }

    public void displayCrashReport(CrashReport crashReportIn) {
        // Separate Hyperium crash reports.
        String data = crashReportIn.getCauseStackTraceOrString();
        File crashReportDir;
        String crashReportPrefix = "crash-";
        if (data.contains("hyperium")) {
            crashReportDir = new File(Minecraft.getMinecraft().mcDataDir, "jb-crash-reports");
            if (!crashReportDir.exists()) crashReportDir.mkdir();
            crashReportPrefix = "jb-crash-";
        } else {
            crashReportDir = new File(Minecraft.getMinecraft().mcDataDir, "crash-reports");
        }

        File crashReportFile = new File(crashReportDir,
            crashReportPrefix + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-jbc.txt"
        );

        crashReportIn.saveToFile(crashReportFile);
        Bootstrap.printToSYSOUT(crashReportIn.getCompleteReport());

        try {
            Display.setFullscreen(false);
            Display.setDisplayMode(new DisplayMode(720, 480));
            Display.update();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int crashAction = CrashReportGUI.handle(crashReportIn);

        switch (crashAction) {
            case 0:
                System.exit(-1);
                break;
            case 1:
                try {
                    parent.shutdown();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.exit(-1);
                }
                break;
            case 2:
                try {
                    // Restart the client using the command line.
                    StringBuilder cmd = new StringBuilder();
                    String[] command = System.getProperty("sun.java.command").split(" ");
                    cmd.append(System.getProperty("java.home")).append(File.separator).append("bin")
                        .append(File.separator).append("java ");
                    ManagementFactory.getRuntimeMXBean().getInputArguments().forEach(s -> {
                        if (!s.contains("-agentlib")) {
                            cmd.append(s).append(" ");
                        }
                    });
                    if (command[0].endsWith(".jar")) {
                        cmd.append("-jar ").append(new File(command[0]).getPath()).append(" ");
                    } else {
                        cmd.append("-cp \"").append(System.getProperty("java.class.path"))
                            .append("\" ").append(command[0]).append(" ");
                    }
                    for (int i = 1; i < command.length; i++) {
                        cmd.append(command[i]).append(" ");
                    }
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        try {
                            Runtime.getRuntime().exec(cmd.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }));
                    parent.shutdown();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
        }
    }

    public void shutdown() {
        AddonMinecraftBootstrap.getLoadedAddons().forEach(IAddon::onClose);
    }
}
