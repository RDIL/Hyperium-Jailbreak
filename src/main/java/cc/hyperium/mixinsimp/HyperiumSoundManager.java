package cc.hyperium.mixinsimp;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.SoundPlayEvent;
import net.minecraft.client.audio.ISound;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.concurrent.locks.ReentrantLock;

public class HyperiumSoundManager {
    private ReentrantLock lock = new ReentrantLock();

    public HyperiumSoundManager() {}

    public void playSound(ISound sound, CallbackInfo ci) {
        if (Settings.SMART_SOUNDS && !Display.isActive()) {
            ci.cancel();
            return;
        }
        SoundPlayEvent e = new SoundPlayEvent(sound);
        EventBus.INSTANCE.post(e);
        if (e.isCancelled()) ci.cancel();
    }

    public void startUpdate(CallbackInfo info) {
        lock.lock();
    }

    public void endUpdate(CallbackInfo info) {
        lock.unlock();
    }

    public void startStopAllSounds(CallbackInfo info) {
        lock.lock();
    }

    public void endStopAllSounds(CallbackInfo info) {
        lock.unlock();
    }
}
