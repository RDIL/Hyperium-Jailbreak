package cc.hyperium.mixins;

import cc.hyperium.config.Settings;
import net.minecraft.profiler.PlayerUsageSnooper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerUsageSnooper.class)
public class MixinSnooper {
    @Inject(method = "startSnooper", at = @At("HEAD"), cancellable = true)
    private void startSnooper(CallbackInfo ci) {
        if(Settings.NOSNOOPER) ci.cancel();
    }
}
