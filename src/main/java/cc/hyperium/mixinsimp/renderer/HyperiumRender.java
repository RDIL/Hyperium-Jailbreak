package cc.hyperium.mixinsimp.renderer;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.render.RenderNameTagEvent;
import cc.hyperium.mixins.renderer.IMixinRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class HyperiumRender<T extends Entity> {
    private Render<T> parent;

    public HyperiumRender(Render<T> parent) {
        this.parent = parent;
    }

    public void renderOffsetLivingLabel(T entityIn, double x, double y, double z, String str) {
        ((IMixinRender) parent).callRenderLivingLabel(entityIn, str, x, y, z, Math.min(64 * 64, Hyperium.INSTANCE.getHandlers().getConfigOptions().renderNameDistance));
    }

    public void renderName(T entity, double x, double y, double z) {
        if (((IMixinRender) parent).callCanRenderName(entity)) {
            ((IMixinRender) parent).callRenderLivingLabel(entity, entity.getDisplayName().getFormattedText(), x, y, z, Math.min(64 * 64, Hyperium.INSTANCE.getHandlers().getConfigOptions().renderNameDistance));
        }
    }

    public void renderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance, RenderManager renderManager) {
        double d0 = entityIn.getDistanceSqToEntity(renderManager.livingPlayer);

        if (d0 <= (double) (maxDistance * maxDistance)) {
            boolean self = entityIn.equals(Minecraft.getMinecraft().thePlayer);
            boolean show = !self || Settings.SHOW_OWN_NAME || RenderNameTagEvent.CANCEL;
            FontRenderer fontrenderer = renderManager.getFontRenderer();
            float f = 1.6F;
            float f1 = 0.016666668F * f;
            GlStateManager.pushMatrix();
            float offset = 0;
            GlStateManager.translate((float) x + 0.0F, (float) y + offset + entityIn.height + 0.5F, (float) z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);

            int xMultiplier = 1; // Nametag x rotations should flip in front-facing 3rd person
            if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().gameSettings != null && Minecraft.getMinecraft().gameSettings.thirdPersonView == 2)
                xMultiplier = -1;
            GlStateManager.rotate(renderManager.playerViewX * xMultiplier, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-f1, -f1, f1);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();

            if (show) {
                int j = fontrenderer.getStringWidth(str) / 2;
                GlStateManager.disableTexture2D();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                float a = .25F;
                worldrenderer.pos((-j - 1), (-1), 0.0D).color(0.0F, 0.0F, 0.0F, a).endVertex();
                worldrenderer.pos((-j - 1), (8), 0.0D).color(0.0F, 0.0F, 0.0F, a).endVertex();
                worldrenderer.pos((j + 1), (8), 0.0D).color(0.0F, 0.0F, 0.0F, a).endVertex();
                worldrenderer.pos((j + 1), (-1), 0.0D).color(0.0F, 0.0F, 0.0F, a).endVertex();
                tessellator.draw();
            }
            GlStateManager.enableTexture2D();
            if (show)
                fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, 0, 553648127);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            if (show)
                fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, 0, -1);
            if (entityIn instanceof EntityPlayer && !RenderNameTagEvent.CANCEL) {
                EventBus.INSTANCE.post(new RenderNameTagEvent(((AbstractClientPlayer) entityIn), renderManager));
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            }
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            GlStateManager.popMatrix();
        }
    }
}
