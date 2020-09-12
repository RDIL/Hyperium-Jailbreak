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

package cc.hyperium.cosmetics;

import cc.hyperium.cosmetics.hats.ModelHatFez;
import cc.hyperium.cosmetics.hats.ModelHatLego;
import cc.hyperium.cosmetics.hats.ModelHatTophat;
import cc.hyperium.cosmetics.wings.WingsCosmetic;
import cc.hyperium.event.EventBus;
import net.minecraft.util.ResourceLocation;

public class HyperiumCosmetics {
    public HyperiumCosmetics() {
        registerCosmetic(new WingsCosmetic());
        registerCosmetic(new CosmeticHat("TOPHAT").setModel(new ModelHatTophat(), new ResourceLocation("textures/cosmetics/hats/tophat.png")));
        registerCosmetic(new CosmeticHat("FEZ").setModel(new ModelHatFez(), new ResourceLocation("textures/cosmetics/hats/fez.png")));
        registerCosmetic(new CosmeticHat("LEGO").setModel(new ModelHatLego(), new ResourceLocation("textures/cosmetics/hats/lego.png")));
    }

    private void registerCosmetic(AbstractCosmetic cosmetic) {
        EventBus.INSTANCE.register(cosmetic);
    }
}
