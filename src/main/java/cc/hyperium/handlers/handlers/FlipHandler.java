package cc.hyperium.handlers.handlers;

import cc.hyperium.config.Settings;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.EnumChatFormatting;

public class FlipHandler {
    public void transform(EntityLivingBase bat) {
        String s = EnumChatFormatting.getTextWithoutFormattingCodes(bat.getName());

        if (!Settings.isFlipped) return;

        if (Settings.FLIP_TYPE_STRING.equals("ROTATE") || s != null && (s.equals("Dinnerbone") || s.equals("Grumm")) && (!(bat instanceof EntityPlayer) || ((EntityPlayer) bat).isWearing(EnumPlayerModelParts.CAPE))) {
            float y = bat.height + 0.1F;
            GlStateManager.translate(0.0F, y / 2, 0.0F);
            double l = System.currentTimeMillis() % (360 * 1.75) / 1.75;
            GlStateManager.rotate((float) l, .1F, 0.0F, 0.0F);
            GlStateManager.translate(0.0F, -y / 2, 0.0F);
        } else if (Settings.FLIP_TYPE_STRING.equals("FLIP") || s != null && (s.equals("Dinnerbone") || s.equals("Grumm")) && (!(bat instanceof EntityPlayer) || ((EntityPlayer) bat).isWearing(EnumPlayerModelParts.CAPE))) {
            {
                GlStateManager.translate(0.0F, bat.height + 0.1F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            }
        }
    }
}
