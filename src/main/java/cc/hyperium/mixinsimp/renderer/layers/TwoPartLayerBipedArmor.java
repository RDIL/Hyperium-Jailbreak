package cc.hyperium.mixinsimp.renderer.layers;

import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;

public class TwoPartLayerBipedArmor extends LayerArmorBase<ModelBiped> {
    public TwoPartLayerBipedArmor(RendererLivingEntity<?> rendererIn) {
        super(rendererIn);
    }

    protected void initArmor() {
        this.field_177189_c = new ModelBiped(0.5F);
        this.field_177186_d = new ModelBiped(1.0F);
    }

    protected void func_177179_a(ModelBiped model, int armorSlot) {
        model.setInvisible(false);
        IMixinModelBiped modelBiped = (IMixinModelBiped) model;

        switch (armorSlot) {
            case 1: {
                model.bipedRightLeg.showModel = true;
                model.bipedLeftLeg.showModel = true;
                modelBiped.getBipedRightLowerLeg().showModel = true;
                modelBiped.getBipedLeftLowerLeg().showModel = true;
                break;
            }
            case 2: {
                model.bipedBody.showModel = true;
                model.bipedRightLeg.showModel = true;
                model.bipedLeftLeg.showModel = true;
                modelBiped.getBipedRightLowerLeg().showModel = true;
                modelBiped.getBipedLeftLowerLeg().showModel = true;
                break;
            }
            case 3: {
                model.bipedBody.showModel = true;
                model.bipedRightArm.showModel = true;
                model.bipedLeftArm.showModel = true;
                modelBiped.getBipedRightForeArm().showModel = true;
                modelBiped.getBipedLeftForeArm().showModel = true;
                break;
            }
            case 4: {
                model.bipedHead.showModel = true;
                model.bipedHeadwear.showModel = true;
                break;
            }
        }
    }
}
