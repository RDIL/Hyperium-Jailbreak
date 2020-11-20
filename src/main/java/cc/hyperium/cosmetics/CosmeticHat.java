package cc.hyperium.cosmetics;

import cc.hyperium.config.provider.CosmeticOptionsProvider;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.render.RenderPlayerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class CosmeticHat extends AbstractCosmetic {
    private ModelBase hatModel;
    private ResourceLocation hatTexture;
    private final String id;

    CosmeticHat(String id) {
        this.id = id;
    }

    CosmeticHat setModel(ModelBase givenModel, ResourceLocation givenTexture) {
        this.hatModel = givenModel;
        this.hatTexture = givenTexture;
        return this;
    }

    @InvokeEvent
    public void onPlayerRender(RenderPlayerEvent e) {
        Minecraft mc = Minecraft.getMinecraft();
        AbstractClientPlayer player = e.getEntity();

        if (!CosmeticOptionsProvider.HAT_TYPE.equals(this.id)) return;

        if (player.getUniqueID() == Minecraft.getMinecraft().thePlayer.getUniqueID() && !player.isInvisible()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(e.getX(), e.getY(), e.getZ());
            final double scale = 1.0F;
            final double rotate = interpolate(player.prevRotationYawHead, player.rotationYawHead, e.getPartialTicks());
            final double rotate1 = interpolate(player.prevRotationPitch, player.rotationPitch, e.getPartialTicks());

            GL11.glScaled(-scale, -scale, scale);

            GL11.glTranslated(0.0, -((player.height - (player.isSneaking() ? .25 : 0)) - .38) / scale, 0.0);

            GL11.glRotated(180.0 + rotate, 0.0, 1.0, 0.0);
            GL11.glRotated(rotate1, 1.0D, 0.0D, 0.0D);

            GlStateManager.translate(0, -.45, 0);

            mc.getTextureManager().bindTexture(this.hatTexture);
            hatModel.render(player, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

            GlStateManager.popMatrix();
        }
    }
}
