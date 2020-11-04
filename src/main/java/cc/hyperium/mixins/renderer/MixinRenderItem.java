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

import cc.hyperium.mixinsimp.renderer.HyperiumRenderItem;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
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
    public void renderItemIntoGUI(ItemStack stack, int x, int y) {
        hyperiumRenderItem.renderItemIntoGUI(stack, x, y);
    }
}
