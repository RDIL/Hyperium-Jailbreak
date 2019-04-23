package cc.hyperium.mixinsimp.client.renderer.entity.layers;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class HyperiumLayerArmorBase<T extends ModelBase> implements LayerRenderer<EntityLivingBase> {
    public HyperiumLayerArmorBase() {}

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {}

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
