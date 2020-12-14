package cc.hyperium.mixins.renderer;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderItem.class)
public interface IMixinRenderItem2 {
    @Invoker void callRenderModel(IBakedModel model, int color);
}
