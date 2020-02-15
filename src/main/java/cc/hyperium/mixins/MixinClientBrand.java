package cc.hyperium.mixins;

import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientBrandRetriever.class)
public class MixinClientBrand {
    /**
     * @author Reece Dunham
     */
    @Overwrite
    public static String getClientModName() {
        return "HyperiumJB";
    }
}
