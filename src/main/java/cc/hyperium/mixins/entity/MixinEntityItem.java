package cc.hyperium.mixins.entity;

import net.minecraft.entity.item.EntityItem;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityItem.class)
public class MixinEntityItem {
    @Redirect(method = "getEntityItem", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;)V"))
    public void error(Logger logger, String message) {
        // hypixel does some really odd things with items, and we really don't need to hear about that
    }
}
