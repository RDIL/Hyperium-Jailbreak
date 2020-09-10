package cc.hyperium.mixinsimp.renderer.model;

import net.minecraft.client.model.ModelRenderer;

public interface IMixinModelPlayer extends IMixinModelBiped {
    ModelRenderer getBipedRightUpperLegwear();

    ModelRenderer getBipedRightLowerLegwear();

    ModelRenderer getBipedLeftUpperLegwear();

    ModelRenderer getBipedLeftLowerLegwear();

    ModelRenderer getBipedRightUpperArmwear();

    ModelRenderer getBipedRightForeArmwear();

    ModelRenderer getBipedLeftUpperArmwear();

    ModelRenderer getBipedBodywear();

    ModelRenderer getCape();
}
