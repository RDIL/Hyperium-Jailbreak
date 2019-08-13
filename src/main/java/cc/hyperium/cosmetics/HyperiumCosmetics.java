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
import cc.hyperium.cosmetics.companions.dragon.DragonCompanion;
import cc.hyperium.cosmetics.companions.hamster.HamsterCompanion;
import cc.hyperium.cosmetics.hats.ModelHatFez;
import cc.hyperium.cosmetics.hats.ModelHatLego;
import cc.hyperium.cosmetics.hats.ModelHatTophat;
import cc.hyperium.cosmetics.wings.WingsCosmetic;
import cc.hyperium.event.EventBus;
import cc.hyperium.purchases.EnumPurchaseType;
import net.minecraft.util.ResourceLocation;

public class HyperiumCosmetics {
    private final FlipCosmetic flipCosmetic;
    private final Deadmau5Cosmetic deadmau5Cosmetic;

    public HyperiumCosmetics() {
        registerCosmetic(flipCosmetic = new FlipCosmetic());
        registerCosmetic(deadmau5Cosmetic = new Deadmau5Cosmetic());
        registerCosmetic(new WingsCosmetic());
        registerCosmetic(new DragonCosmetic());
        registerCosmetic(new DragonCompanion());
        registerCosmetic(new HamsterCompanion());
        registerCosmetic(new CosmeticHat(EnumPurchaseType.HAT_TOPHAT).setModel(new ModelHatTophat(), new ResourceLocation("textures/cosmetics/hats/tophat.png")));
        registerCosmetic(new CosmeticHat(EnumPurchaseType.HAT_FEZ).setModel(new ModelHatFez(), new ResourceLocation("textures/cosmetics/hats/fez.png")));
        registerCosmetic(new CosmeticHat(EnumPurchaseType.HAT_LEGO).setModel(new ModelHatLego(), new ResourceLocation("textures/cosmetics/hats/lego.png")));
    }

    private void registerCosmetic(AbstractCosmetic cosmetic) {
        EventBus.INSTANCE.register(cosmetic);
    }

    public FlipCosmetic getFlipCosmetic() {
        return this.flipCosmetic;
    }

    public Deadmau5Cosmetic getDeadmau5Cosmetic() {
        return this.deadmau5Cosmetic;
    }
}
