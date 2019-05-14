package cc.hyperium.mixinsimp.utils;

import net.minecraft.util.ChatComponentStyle;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class HyperiumChatComponentStyle {
    private String cache;
    public HyperiumChatComponentStyle() {}

    public void invalidateCache() {
        this.cache = null;
    }

    public void getFormatedTextHeader(CallbackInfoReturnable<String> string) {
        if (cache != null) string.setReturnValue(cache);
    }

    public void getFormatedTextReturn(CallbackInfoReturnable<String> string) {
        this.cache = string.getReturnValue();
    }
}
