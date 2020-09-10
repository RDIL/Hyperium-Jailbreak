package cc.hyperium.mixinsimp.renderer.model;

import net.minecraft.client.model.ModelRenderer;

public interface IMixinModelBiped {
    ModelRenderer getBipedRightLowerLeg();

    ModelRenderer getBipedLeftLowerLeg();

    ModelRenderer getBipedRightUpperArm();

    ModelRenderer getBipedRightForeArm();

    ModelRenderer getBipedLeftUpperArm();

    ModelRenderer getBipedLeftForeArm();

    ModelRenderer getBipedHead();

    ModelRenderer getBipedHeadwear();
}
