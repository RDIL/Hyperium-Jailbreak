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

package cc.hyperium.cosmetics.wings;

import cc.hyperium.cosmetics.AbstractCosmetic;
import cc.hyperium.event.EventBus;
import cc.hyperium.purchases.EnumPurchaseType;
import net.minecraft.util.ResourceLocation;

public class WingsCosmetic extends AbstractCosmetic {
    private final ResourceLocation dragon = new ResourceLocation("textures/cosmetics/wings/dragonwings.png");

    public WingsCosmetic() {
        super(EnumPurchaseType.WING_COSMETIC);
        EventBus.INSTANCE.register(new WingsRenderer(this));
    }

    public ResourceLocation getLocation() {
        return dragon;
    }
}
