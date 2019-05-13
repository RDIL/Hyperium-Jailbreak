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

package cc.hyperium.mixins;

import cc.hyperium.gui.SplashProgress;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.RenderTickEvent;
import cc.hyperium.event.WorldLoadEvent;
import cc.hyperium.mixinsimp.HyperiumMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.Timer;
import net.minecraft.world.WorldSettings;
import org.lwjgl.LWJGLException;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.io.File;
import java.util.List;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Shadow
    public FontRenderer fontRendererObj;

    @Shadow
    @Final
    public Profiler mcProfiler;

    @Shadow
    public boolean inGameHasFocus;

    @Shadow
    public int displayHeight;

    @Shadow
    public int displayWidth;

    @Shadow
    public GuiScreen currentScreen;

    @Shadow
    public WorldClient theWorld;

    @Shadow
    public EntityPlayerSP thePlayer;

    @Shadow
    public GameSettings gameSettings;

    @Shadow
    public GuiIngame ingameGUI;

    @Shadow
    private boolean fullscreen;

    @Shadow
    @Final
    private DefaultResourcePack mcDefaultResourcePack;

    @Shadow
    @Final
    private List<IResourcePack> defaultResourcePacks;

    @Shadow
    private boolean enableGLErrorChecking;

    private HyperiumMinecraft hyperiumMinecraft = new HyperiumMinecraft((Minecraft) (Object) this);

    @Shadow
    private Timer timer;

    @Shadow
    private RenderManager renderManager;

    @Shadow
    long systemTime;

    @Final
    @Shadow
    public File mcDataDir;

    @Inject(method = "startGame", at = @At("HEAD"))
    private void preinit(CallbackInfo ci) {
        hyperiumMinecraft.preinit(defaultResourcePacks, mcDefaultResourcePack);
    }

    @Inject(method = "runGameLoop", at = @At("HEAD"))
    public void loop(CallbackInfo info) {
        hyperiumMinecraft.loop(inGameHasFocus, theWorld, thePlayer, renderManager, timer);
    }

    @Inject(method = "startGame", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        enableGLErrorChecking = false;
        hyperiumMinecraft.startGame();
    }

    @Inject(method = "runTick", at = @At("RETURN"))
    private void runTick(CallbackInfo ci) {
        hyperiumMinecraft.runTick(mcProfiler);
    }

    @Inject(method = "dispatchKeypresses", at = @At(value = "INVOKE_ASSIGN", target = "Lorg/lwjgl/input/Keyboard;getEventKeyState()Z"))
    private void runTickKeyboard(CallbackInfo ci) {
        hyperiumMinecraft.runTickKeyboard();
    }

    @Inject(method = "clickMouse", at = @At("RETURN"))
    private void clickMouse(CallbackInfo ci) {
        hyperiumMinecraft.clickMouse();
    }

    @Inject(method = "rightClickMouse", at = @At("RETURN"))
    private void rightClickMouse(CallbackInfo ci) {
        hyperiumMinecraft.rightClickMouse();
    }

    @Inject(method = "launchIntegratedServer", at = @At("HEAD"))
    private void launchIntegratedServer(String folderName, String worldName, WorldSettings worldSettingsIn, CallbackInfo ci) {
        hyperiumMinecraft.launchIntegratedServer();
    }

    @Inject(method = "setInitialDisplayMode", at = @At(value = "HEAD"), cancellable = true)
    private void displayFix(CallbackInfo ci) throws LWJGLException {
        hyperiumMinecraft.displayFix(ci, fullscreen, displayWidth, displayHeight);
    }

    @Inject(method = "toggleFullscreen", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;setVSyncEnabled(Z)V", shift = At.Shift.AFTER))
    private void fullScreenFix(CallbackInfo ci) throws LWJGLException {
        hyperiumMinecraft.fullScreenFix(fullscreen, displayWidth, displayHeight);
    }

    @Overwrite
    public void displayGuiScreen(GuiScreen guiScreenIn) {
        hyperiumMinecraft.displayGuiScreen(guiScreenIn, currentScreen, theWorld, thePlayer, gameSettings, ingameGUI);
    }

    @Shadow
    public abstract void shutdown();

    @Shadow
    public abstract void run();

    @Shadow
    public EffectRenderer effectRenderer;

    @Overwrite
    private void drawSplashScreen(TextureManager tm) {
        SplashProgress.drawSplash(tm);
    }

    @Inject(method = "startGame", at = @At("HEAD"))
    private void onStartGame(CallbackInfo ci) {
        hyperiumMinecraft.onStartGame();
    }

    @Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;)V", at = @At("HEAD"))
    private void loadWorld(WorldClient worldClient, CallbackInfo ci) {
        hyperiumMinecraft.loadWorld();
    }

    @Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "Ljava/lang/System;gc()V"), cancellable = true)
    public void fixGarbageCollection(WorldClient worldClientIn, String loadingMessage, CallbackInfo info) {
        new WorldLoadEvent().post();
        if (!Settings.FAST_WORLD_LOADING) return;
        systemTime = 0;
        info.cancel();
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Mouse;getEventButton()I", ordinal = 0))
    private void runTickMouseButton(CallbackInfo ci) {
        hyperiumMinecraft.runTickMouseButton(ci);
    }

    @Overwrite
    public void displayCrashReport(CrashReport crashReportIn) {
        hyperiumMinecraft.displayCrashReport(crashReportIn);
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    private void shutdown(CallbackInfo ci) {
        hyperiumMinecraft.shutdown();
    }

    @Inject(method = "runGameLoop", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;skipRenderWorld:Z", shift = At.Shift.AFTER))
    private void runGameLoop(CallbackInfo callbackInfo) {
        EventBus.INSTANCE.post(new RenderTickEvent());
    }
}
