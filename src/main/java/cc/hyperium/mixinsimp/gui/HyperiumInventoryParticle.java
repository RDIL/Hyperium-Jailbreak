package cc.hyperium.mixinsimp.gui;

import cc.hyperium.gui.ParticleOverlay;
import net.minecraft.client.Minecraft;

public class HyperiumInventoryParticle {
    public HyperiumInventoryParticle() {}

    public void draw(int mouseX, int mouseY, int guiLeft, int xSize, int guiTop) {
        ParticleOverlay overlay = ParticleOverlay.getOverlay();
        if (overlay.getMode() == ParticleOverlay.Mode.OFF) return;
        overlay.render(mouseX, mouseY, guiLeft - (Minecraft.getMinecraft().thePlayer.getActivePotionEffects().isEmpty() ? 0 : xSize * 3 / 4), guiTop - 5, guiLeft + (240 * 4 / 5), guiTop + (240 * 4 / 5 - 10));
    }
}
