package cc.hyperium.mixins.renderer;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RendererLivingEntity.class)
public interface IMixinRenderLivingEntity<T extends EntityLivingBase> {
    @Invoker
    boolean callCanRenderName(T entity);
}
