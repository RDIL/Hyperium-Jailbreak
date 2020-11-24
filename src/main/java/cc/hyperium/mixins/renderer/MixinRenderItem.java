/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.renderer;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.render.ItemGetDurabilityEvent;
import cc.hyperium.mixinsimp.renderer.HyperiumRenderItem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(RenderItem.class)
public abstract class MixinRenderItem implements IResourceManagerReloadListener {
    private HyperiumRenderItem hyperiumRenderItem = new HyperiumRenderItem((RenderItem) (Object) this);

    /**
     * @author hyperium
     */
    @Overwrite
    public void renderItem(ItemStack stack, IBakedModel model) {
        hyperiumRenderItem.renderItem(stack, model, false);
    }

    /**
     * @author hyperium
     */
    @Overwrite
    private void renderModel(IBakedModel model, int color, ItemStack stack) {
        hyperiumRenderItem.renderModel(model, color, stack);
    }

    /**
     * @author hyperium
     */
    @Overwrite
    public void renderItemIntoGUI(ItemStack stack, int x, int y) {
        hyperiumRenderItem.renderItemIntoGUI(stack, x, y);
    }

    private static double getDurabilityForDisplay(ItemStack stack) {
        final double val = (double) stack.getItemDamage() / (double) stack.getMaxDamage();
        final ItemGetDurabilityEvent gde = new ItemGetDurabilityEvent(stack, val);
        EventBus.INSTANCE.post(gde);
        return gde.durability;
    }

    // TODO: make this use an inject instead of nuking it
    /**
     * @author Reece Dunham
     */
    @Overwrite
    public void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, String text) {
        if (stack != null) {
            if (stack.stackSize != 1 || text != null) {
                String s = text == null ? String.valueOf(stack.stackSize) : text;

                if (text == null && stack.stackSize < 1) {
                    s = EnumChatFormatting.RED + String.valueOf(stack.stackSize);
                }

                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableBlend();
                fr.drawStringWithShadow(s, (float) (xPosition + 19 - 2 - fr.getStringWidth(s)), (float)(yPosition + 6 + 3), 16777215);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }

            if (stack.isItemDamaged()) {
                double health = getDurabilityForDisplay(stack);
                int j = (int) Math.round(13.0D - health * 13.0D);
                int i = (int) Math.round(255.0D - health * 255.0D);
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableTexture2D();
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                ((IMixinRenderItem) this).callFunc_181565_a(worldrenderer, xPosition + 2, yPosition + 13, 13, 2, 0, 0, 0, 255);
                ((IMixinRenderItem) this).callFunc_181565_a(worldrenderer, xPosition + 2, yPosition + 13, 12, 1, (255 - i) / 4, 64, 0, 255);
                ((IMixinRenderItem) this).callFunc_181565_a(worldrenderer, xPosition + 2, yPosition + 13, j, 1, 255 - i, i, 0, 255);
                GlStateManager.enableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }
    }
}
