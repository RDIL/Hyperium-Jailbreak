package cc.hyperium.mixinsimp.renderer;

import cc.hyperium.config.provider.AnimationOptionsProvider;
import cc.hyperium.config.provider.GeneralOptionsProvider;
import cc.hyperium.mixins.renderer.IMixinRender;
import cc.hyperium.mixins.renderer.IMixinRenderLivingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class HyperiumRenderLivingEntity<T extends EntityLivingBase> {
    private RendererLivingEntity<T> parent;

    public HyperiumRenderLivingEntity(RendererLivingEntity<T> parent) {
        this.parent = parent;
    }

    public void renderLayers(T entitylivingbaseIn, float p_177093_2_, float p_177093_3_, float partialTicks, float p_177093_5_, float p_177093_6_, float p_177093_7_, float p_177093_8_, List<LayerRenderer<T>> layerRenderers) {
        for (LayerRenderer<T> layerrenderer : layerRenderers) {
            boolean f = layerrenderer.shouldCombineTextures();
            if (AnimationOptionsProvider.OLD_ARMOUR) {
                f = true;
            }
            boolean flag = ((IMixinRenderLivingEntity<T>) parent).callSetBrightness(entitylivingbaseIn, partialTicks, f);

            layerrenderer.doRenderLayer(entitylivingbaseIn, p_177093_2_, p_177093_3_, partialTicks, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_);

            if (flag) {
                ((IMixinRenderLivingEntity) parent).callUnsetBrightness();
            }
        }
    }

    public void renderName(T entity, double x, double y, double z, RenderManager renderManager) {
        if (((IMixinRenderLivingEntity<T>) parent).callCanRenderName(entity)) {
            double d0 = entity.getDistanceSqToEntity(renderManager.livingPlayer);
            float f = entity.isSneaking() ? 32.0F : 64.0F;

            if (d0 < (double) (f * f)) {
                String s = entity.getDisplayName().getFormattedText();
                GlStateManager.alphaFunc(516, 0.1F);

                if (entity.isSneaking() && (GeneralOptionsProvider.SHOW_OWN_NAME || !entity.equals(Minecraft.getMinecraft().thePlayer))) {
                    FontRenderer fontrenderer = renderManager.getFontRenderer();
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float) x, (float) y + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0F), (float) z);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                    GlStateManager.scale(-0.02666667F, -0.02666667F, 0.02666667F);
                    GlStateManager.translate(0.0F, 9.374999F, 0.0F);
                    GlStateManager.disableLighting();
                    GlStateManager.depthMask(false);
                    GlStateManager.enableBlend();
                    GlStateManager.disableTexture2D();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    int i = fontrenderer.getStringWidth(s) / 2;
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                    worldrenderer.pos((-i - 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos((-i - 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos((i + 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos((i + 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    tessellator.draw();
                    GlStateManager.enableTexture2D();
                    GlStateManager.depthMask(true);
                    fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.popMatrix();
                } else {
                    ((IMixinRender<T>) parent).callRenderOffsetLivingLabel(entity, x, y - (entity.isChild() ? (double) (entity.height / 2.0F) : 0.0D), z, s, 0.02666667F, d0);
                }
            }
        }
    }
}
