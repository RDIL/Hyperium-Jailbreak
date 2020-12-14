package cc.hyperium.mixins.renderer;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.IBakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderItem.class)
public interface IMixinRenderItem {
    @Accessor TextureManager getTextureManager();
    @Invoker void callRenderModel(IBakedModel model, int color);
    @Invoker void callFunc_181565_a(WorldRenderer wr, int a, int b, int c, int d, int e, int f, int g, int h);
}
