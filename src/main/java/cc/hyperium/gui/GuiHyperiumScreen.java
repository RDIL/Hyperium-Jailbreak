package cc.hyperium.gui;

import cc.hyperium.styles.GuiStyle;
import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.gui.playerrenderer.GuiPlayerRenderer;
import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import java.awt.Font;

public class GuiHyperiumScreen extends GuiScreen {
    private static final ResourceLocation background = new ResourceLocation("textures/material/backgrounds/1.png");
    public static HyperiumFontRenderer fr = new HyperiumFontRenderer("Arial", Font.PLAIN, 20);
    static DynamicTexture viewportTexture;
    private static float swing;
    GuiButton hypixelButton;

    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_POINT_SMOOTH);
        GL11.glHint(GL11.GL_POINT_SMOOTH_HINT, GL11.GL_FASTEST);

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, (y + height), 0.0D).tex((u * f), ((v + (float) vHeight) * f1)).endVertex();
        worldrenderer.pos((x + width), (y + height), 0.0D).tex(((u + (float) uWidth) * f), ((v + (float) vHeight) * f1)).endVertex();
        worldrenderer.pos((x + width), y, 0.0D).tex(((u + (float) uWidth) * f), (v * f1)).endVertex();
        worldrenderer.pos(x, y, 0.0D).tex((u * f), (v * f1)).endVertex();
        tessellator.draw();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_POINT_SMOOTH);
    }

    public void initGui() {}

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (getStyle() == GuiStyle.DEFAULT) {
            drawDefaultStyleScreen(mouseX, mouseY);
        } else {
            drawHyperiumStyleScreen(mouseX, mouseY, partialTicks);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    GuiStyle getStyle() {
        return GuiStyle.valueOf(Minecraft.getMinecraft().theWorld == null ? Settings.MENU_STYLE : Settings.PAUSE_STYLE);
    }

    private void renderHyperiumBackground(ScaledResolution p_180476_1_) {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableAlpha();
        Minecraft.getMinecraft().getTextureManager().bindTexture(background);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0D, p_180476_1_.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
        worldrenderer.pos(p_180476_1_.getScaledWidth(), p_180476_1_.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
        worldrenderer.pos(p_180476_1_.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void drawHyperiumStyleScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();
        swing++;

        if (mc.theWorld == null) {
            GlStateManager.disableAlpha();
            ScaledResolution sr = new ScaledResolution(mc);
            this.renderHyperiumBackground(sr);
            GlStateManager.enableAlpha();
        }

        /* Render shadowed bar at top of screen */
        if (mc.theWorld == null) {
            this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
            this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        } else {
            this.drawDefaultBackground();
        }

        drawRect(0, 4, width, 55, 0x33000000);
        drawRect(0, 5, width, 54, 0x33000000);

        /* Render Client Logo */
        GlStateManager.color(1, 1, 1, 1);
        ResourceLocation logo = new ResourceLocation("textures/hyperium-logo.png");
        Minecraft.getMinecraft().getTextureManager().bindTexture(logo);
        drawScaledCustomSizeModalRect(10, 5, 0, 0, 2160, 500, 200, 47, 2160, 500);

        /* Render profile container */
        drawRect(width - 155, 10, width - 10, 49, 0x33000000);
        drawRect(width - 156, 9, width - 9, 50, 0x33000000);

        float val = (float) (Math.sin(swing / 40) * 30);
        ScissorState.scissor(width - 153, 0, 145, 49, true);

        if (Minecraft.getMinecraft().thePlayer == null || Hyperium.INSTANCE.isDevEnv()) {
            GlStateManager.popMatrix();
            return;
        }

        GuiPlayerRenderer.renderPlayerWithRotation(width - 118, -4, val);
        ScissorState.endScissor();
        fr.drawStringScaled(Minecraft.getMinecraft().thePlayer.getName(), width - 152, 39, 0xFFFFFF, .75);

        GlStateManager.popMatrix();
    }

    private void drawDefaultStyleScreen(int mouseX, int mouseY) {
        GlStateManager.disableAlpha();
        this.renderHyperiumBackground(new ScaledResolution(mc));

        ParticleOverlay.getOverlay().render(mouseX, mouseY, 0, 0, 0, 0);
        GlStateManager.enableAlpha();
        this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
        this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        GlStateManager.pushMatrix();
        GlStateManager.scale(4F, 4F, 1F);
        this.drawCenteredString(fontRendererObj, Hyperium.modid, width / 8, 40 / 4, 0xFFFFFF);
        GlStateManager.popMatrix();
        String s = String.format("%s %s", Hyperium.modid, Hyperium.version);
        this.drawString(this.fontRendererObj, s, 2, this.height - 10, -1);
        String s1 = "Not Affiliated with Mojang AB";
        this.drawString(this.fontRendererObj, s1, this.width - this.fontRendererObj.getStringWidth(s1) - 2, this.height - 10, -1);
        String s3 = "Made by Hyperium devs";
        this.drawString(this.fontRendererObj, s3, this.width - this.fontRendererObj.getStringWidth(s3) - 2, this.height - 30, -1);
        String s4 = "and jumbo <3";
        this.drawString(this.fontRendererObj, s4, this.width - this.fontRendererObj.getStringWidth(s4) - 2, this.height - 20, -1);
        GuiButton hypixelButton = this.hypixelButton;
        if (hypixelButton != null) hypixelButton.displayString = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? "Fix Hypixel Session" : "Join Hypixel";
    }

    int getIntendedWidth(int value) {
        float intendedWidth = 1920F;
        return (int) ((Minecraft.getMinecraft().displayWidth / intendedWidth) * value);
    }

    int getIntendedHeight(int value) {
        float intendedHeight = 1080F;
        return (int) ((Minecraft.getMinecraft().displayHeight / intendedHeight) * value);
    }

    @Override
    public void drawDefaultBackground() {
        this.drawWorldBackground(0);
    }

    @Override
    public void drawWorldBackground(int tint) {
        if (this.mc.theWorld != null) {
            this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
        } else {
            this.drawBackground(tint);
        }
    }
}
