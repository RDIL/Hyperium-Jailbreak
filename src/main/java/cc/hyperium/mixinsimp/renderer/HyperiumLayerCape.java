package cc.hyperium.mixinsimp.renderer;

import cc.hyperium.Hyperium;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;

public class HyperiumLayerCape {
    public HyperiumLayerCape() {}

    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn) {
        if (entitylivingbaseIn.isSneaking() && !Hyperium.INSTANCE.isOptifineInstalled()) GlStateManager.translate(0, 0, .1);
    }
}
