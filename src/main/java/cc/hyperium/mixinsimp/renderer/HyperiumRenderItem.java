package cc.hyperium.mixinsimp.renderer;
import cc.hyperium.config.Settings;
import cc.hyperium.mixins.renderer.IMixinRenderItem;
import cc.hyperium.mixins.renderer.IMixinRenderItem2;
import cc.hyperium.mods.glintcolorizer.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class HyperiumRenderItem {
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    private RenderItem parent;

    public HyperiumRenderItem(RenderItem parent) {
        this.parent = parent;
    }

    public void renderItemIntoGUI(ItemStack stack, int x, int y) {
        IBakedModel ibakedmodel = parent.getItemModelMesher().getItemModel(stack);
        GlStateManager.pushMatrix();
        ((IMixinRenderItem) parent).getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        ((IMixinRenderItem) parent).getTextureManager().getTexture(TextureMap.locationBlocksTexture)
            .setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ((IMixinRenderItem) parent).callSetupGuiTransform(x, y, ibakedmodel.isGui3d());
        ibakedmodel.getItemCameraTransforms()
            .applyTransform(ItemCameraTransforms.TransformType.GUI);

        this.renderItem(stack, ibakedmodel, true); // Changed to true because this IS an inventory

        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        ((IMixinRenderItem) parent).getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        ((IMixinRenderItem) parent).getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
    }

    public void renderItem(ItemStack stack, IBakedModel model, boolean isInv) {
        if (stack != null) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);

            boolean isHead = !isInv && stack.getItem() != null && stack.getItem() instanceof ItemSkull;
            double headScale = Settings.HEAD_SCALE_FACTOR;

            if (model.isBuiltInRenderer()) {
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(-0.5F, -0.5F, -0.5F);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableRescaleNormal();
                if (isHead) GlStateManager.scale(headScale, headScale, headScale);
                TileEntityItemStackRenderer.instance.renderByItem(stack);
                if (isHead) GlStateManager.scale(1.0 / headScale, 1.0 / headScale, 1.0 / headScale);
            } else {
                // Used to detect if the item has a already had an effect rendered
                boolean renderedAsPotion = false;
                GlStateManager.translate(-0.5F, -0.5F, -0.5F);

                // We want to render our potion effect before the item is rendered so it doesn't obscure the item
                if (Settings.SHINY_POTS && isInv && stack.getItem() != null && stack.getItem() instanceof ItemPotion) {
                    renderPot(model); // Use our renderer instead of the normal one
                    renderedAsPotion = true;
                }

                // BigHead implementation
                if (isHead) GlStateManager.scale(headScale, headScale, headScale);

                // Normal item renderer
                ((IMixinRenderItem2) parent).callRenderModel(model, stack);

                // Prevent double-rendering of the items effects
                if (!renderedAsPotion && stack.hasEffect()) this.renderEffect(model);

                // BigHead implementation
                if (isHead) GlStateManager.scale(1.0 / headScale, 1.0 / headScale, 1.0 / headScale);
            }
            GlStateManager.popMatrix();
        }
    }

    public void renderPot(IBakedModel model) {
        GlStateManager.depthMask(false);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(768, 1);
        ((IMixinRenderItem) parent).getTextureManager().bindTexture(RES_ITEM_GLINT);
        GlStateManager.matrixMode(5890);
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
        GlStateManager.translate(f, 0.0F, 0.0F);
        GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
        ((IMixinRenderItem) parent).callRenderModel(model, -8372020);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f1 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
        GlStateManager.translate(-f1, 0.0F, 0.0F);
        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
        ((IMixinRenderItem) parent).callRenderModel(model, -8372020);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);

        ((IMixinRenderItem) parent).getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
    }

    private void renderEffect(IBakedModel model) {
        if (Settings.DISABLE_ENCHANT_GLINT) return;
        GlStateManager.depthMask(false);
        GlStateManager.depthFunc(514);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(768, 1);
        ((IMixinRenderItem) parent).getTextureManager().bindTexture(RES_ITEM_GLINT);
        GlStateManager.matrixMode(5890);
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F; // Animates the effect
        GlStateManager.translate(f, 0.0F, 0.0F);
        GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
        ((IMixinRenderItem) parent).callRenderModel(model, Colors.onepoint8glintcolorI);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f1 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
        GlStateManager.translate(-f1, 0.0F, 0.0F);
        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
        ((IMixinRenderItem) parent).callRenderModel(model, Colors.onepoint8glintcolorI);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableLighting();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        ((IMixinRenderItem) parent).getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
    }

    public void renderModel(IBakedModel model, int color, ItemStack stack) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.ITEM);

        for (EnumFacing enumfacing : EnumFacing.values()) {
            ((IMixinRenderItem) parent).callRenderQuads(worldrenderer, model.getFaceQuads(enumfacing), color, stack);
        }

        ((IMixinRenderItem) parent).callRenderQuads(worldrenderer, model.getGeneralQuads(), color, stack);
        tessellator.draw();
    }
}
