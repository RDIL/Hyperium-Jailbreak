package cc.hyperium.mixinsimp.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.bossbar.config.BossbarConfig;
import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.render.RenderHUDEvent;
import cc.hyperium.mods.chromahud.displayitems.hyperium.ScoreboardDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class HyperiumGuiIngame {
    private GuiIngame parent;
    public static boolean renderScoreboard = true;
    public static boolean renderHealth = true;
    public static boolean renderArmor = true;
    public static boolean renderFood = true;
    public static boolean renderHealthMount = true;
    public static boolean renderAir = true;

    public HyperiumGuiIngame(GuiIngame parent) {
        this.parent = parent;
    }

    public void renderSelectedItem() {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void renderGameOverlay(float part) {
        EventBus.INSTANCE.post(new RenderHUDEvent(part));
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void renderScoreboard(ScoreObjective objective, ScaledResolution resolution) {
        ScoreboardDisplay.p_180475_1_ = objective;
        ScoreboardDisplay.p_180475_2_ = resolution;
        if (renderScoreboard) Hyperium.INSTANCE.getHandlers().getScoreboardRenderer().render(objective, resolution);
    }

    public void renderBossHealth() {
        if (BossStatus.bossName != null && BossStatus.statusBarTime > 0 && BossbarConfig.bossBarEnabled) {
            --BossStatus.statusBarTime;

            ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
            int i = scaledresolution.getScaledWidth();
            if (!BossbarConfig.barEnabled && BossbarConfig.textEnabled) {
                String s = BossStatus.bossName;
                if (BossbarConfig.x != -1) {
                    parent.getFontRenderer().drawStringWithShadow(s, (float) (BossbarConfig.x + 91 - parent.getFontRenderer().getStringWidth(s) / 2), BossbarConfig.y - 10, 16777215);
                } else {
                    parent.getFontRenderer().drawStringWithShadow(s, (float) (i / 2 - parent.getFontRenderer().getStringWidth(s) / 2), BossbarConfig.y - 10, 16777215);
                }
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.icons);
                return;
            }
            int j = 182;
            if (BossbarConfig.barEnabled) {
                int l = (int) (BossStatus.healthScale * (float) (j + 1));
                if (BossbarConfig.x != -1) {
                    parent.drawTexturedModalRect(BossbarConfig.x, BossbarConfig.y, 0, 74, j, 5);
                    if (l > 0) {
                        parent.drawTexturedModalRect(BossbarConfig.x, BossbarConfig.y, 0, 79, l, 5);
                    }
                } else {
                    int k = i / 2 - j / 2;
                    parent.drawTexturedModalRect(k, BossbarConfig.y, 0, 74, j, 5);
                    if (l > 0) {
                        parent.drawTexturedModalRect(k, BossbarConfig.y, 0, 79, l, 5);
                    }
                }
            }

            String s = BossStatus.bossName;
            if (BossbarConfig.textEnabled) {
                if (BossbarConfig.x != -1) {
                    parent.getFontRenderer().drawStringWithShadow(s, (float) (BossbarConfig.x + j / 2 - parent.getFontRenderer().getStringWidth(s) / 2), BossbarConfig.y - 10, 16777215);
                } else {
                    parent.getFontRenderer().drawStringWithShadow(s, (float) (i / 2 - parent.getFontRenderer().getStringWidth(s) / 2), BossbarConfig.y - 10, 16777215);
                }
            }
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.icons);
        }
    }

    public void showCrosshair(CallbackInfoReturnable<Boolean> ci) {
        if (Settings.CROSSHAIR_IN_F5 && Minecraft.getMinecraft().gameSettings.thirdPersonView > 0) {
            ci.setReturnValue(false);
        }

        if (CustomCrosshairAddon.getCrosshairMod() == null) {
            return;
        }

        if (CustomCrosshairAddon.getCrosshairMod().getCrosshair() == null) {
            return;
        }

        if (CustomCrosshairAddon.getCrosshairMod().getCrosshair().getEnabled()) {
            ci.setReturnValue(false);
        }
    }
}
