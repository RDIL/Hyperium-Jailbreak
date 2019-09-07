package cc.hyperium.mixins.utils;

import cc.hyperium.mixinsimp.utils.HyperiumChatStyle;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatStyle.class)
public abstract class MixinChatStyle {
    @Shadow
    private ChatStyle parentStyle;

    private HyperiumChatStyle hyperiumChatStyle = new HyperiumChatStyle((ChatStyle) (Object) (this));

    @Shadow
    public abstract boolean isEmpty();

    @Shadow
    public abstract EnumChatFormatting getColor();

    @Shadow
    public abstract boolean getBold();

    @Overwrite
    public String getFormattingCode() {
        return hyperiumChatStyle.getFormattingCode(parentStyle);
    }

    @Inject(method = "setColor", at = @At("HEAD"))
    private void setColor(EnumChatFormatting color, CallbackInfoReturnable<ChatStyle> info) {
        hyperiumChatStyle.resetCache();
    }

    @Inject(method = "setBold", at = @At("HEAD"))
    private void setBold(Boolean boldIn, CallbackInfoReturnable<ChatStyle> cir) {
        hyperiumChatStyle.resetCache();
    }

    @Inject(method = "setItalic", at = @At("HEAD"))
    private void setItalic(Boolean boldIn, CallbackInfoReturnable<ChatStyle> cir) {
        hyperiumChatStyle.resetCache();
    }

    @Inject(method = "setStrikethrough", at = @At("HEAD"))
    private void setStrikethrough(Boolean boldIn, CallbackInfoReturnable<ChatStyle> cir) {
        hyperiumChatStyle.resetCache();
    }

    @Inject(method = "setUnderlined", at = @At("HEAD"))
    private void setUnderlined(Boolean boldIn, CallbackInfoReturnable<ChatStyle> cir) {
        hyperiumChatStyle.resetCache();
    }

    @Inject(method = "setObfuscated", at = @At("HEAD"))
    private void setObfuscated(Boolean boldIn, CallbackInfoReturnable<ChatStyle> cir) {
        hyperiumChatStyle.resetCache();
    }

    @Inject(method = "setChatClickEvent", at = @At("HEAD"))
    private void setChatClickEvent(ClickEvent boldIn, CallbackInfoReturnable<ChatStyle> cir) {
        hyperiumChatStyle.resetCache();
    }

    @Inject(method = "setChatHoverEvent", at = @At("HEAD"))
    private void setChatHoverEvent(HoverEvent boldIn, CallbackInfoReturnable<ChatStyle> cir) {
        hyperiumChatStyle.resetCache();
    }

    @Inject(method = "setInsertion", at = @At("HEAD"))
    private void setChatHoverEvent(String boldIn, CallbackInfoReturnable<ChatStyle> cir) {
        hyperiumChatStyle.resetCache();
    }

    @Inject(method = "setParentStyle", at = @At("HEAD"))
    private void setChatHoverEvent(ChatStyle boldIn, CallbackInfoReturnable<ChatStyle> cir) {
        hyperiumChatStyle.resetCache();
    }
}
