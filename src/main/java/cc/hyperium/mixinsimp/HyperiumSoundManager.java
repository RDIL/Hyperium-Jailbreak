package cc.hyperium.mixinsimp;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.world.audio.SoundPlayEvent;
import net.minecraft.client.audio.ISound;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class HyperiumSoundManager {
    public static void playSound(ISound sound, CallbackInfo ci) {
        if (Settings.SMART_SOUNDS && !Display.isActive()) {
            ci.cancel();
            return;
        }
        SoundPlayEvent e = new SoundPlayEvent(sound);
        EventBus.INSTANCE.post(e);
        if (e.isCancelled()) ci.cancel();
    }
}
