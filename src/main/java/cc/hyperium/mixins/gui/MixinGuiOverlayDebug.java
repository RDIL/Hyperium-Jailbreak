package cc.hyperium.mixins.gui;

import cc.hyperium.config.Settings;
import cc.hyperium.mixins.IMixinMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiOverlayDebug.class)
public abstract class MixinGuiOverlayDebug {
    @Shadow @Final private Minecraft mc;

    @Redirect(method = "renderDebugInfo", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiOverlayDebug;renderDebugInfoLeft()V"))
    public void renderDebugInfoLeft(GuiOverlayDebug guiOverlayDebug) {
        if (Settings.OLD_DEBUG) {
            this.renderOldDebugInfoLeft();
            return;
        }

        ((IMixinGuiOverlayDebug) guiOverlayDebug).callRenderDebugInfoLeft();
    }

    @Redirect(method = "renderDebugInfo", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiOverlayDebug;renderDebugInfoRight(Lnet/minecraft/client/gui/ScaledResolution;)V"))
    public void renderDebugInfoRight(GuiOverlayDebug guiOverlayDebug, ScaledResolution sr) {
        if (Settings.OLD_DEBUG) {
            this.renderOldDebugInfoRight(sr);
            return;
        }

        ((IMixinGuiOverlayDebug) guiOverlayDebug).callRenderDebugInfoRight(sr);
    }

    protected void renderOldDebugInfoLeft() {
        FontRenderer fontRendererObj = this.mc.fontRendererObj;
        ((IMixinMinecraft) this.mc).getDebugFPS();
        fontRendererObj.drawStringWithShadow("HyperiumJailbreak (" + ((IMixinMinecraft) this.mc).getDebugFPS() + " fps, " + RenderChunk.renderChunksUpdated + " chunk updates)", 2, 2, 16777215);
        fontRendererObj.drawStringWithShadow(this.mc.renderGlobal.getDebugInfoRenders(), 2, 12, 16777215);
        fontRendererObj.drawStringWithShadow(this.mc.renderGlobal.getDebugInfoEntities(), 2, 22, 16777215);
        fontRendererObj.drawStringWithShadow("P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.theWorld.getDebugLoadedEntities(), 2, 32, 16777215);
        fontRendererObj.drawStringWithShadow(this.mc.theWorld.getProviderName(), 2, 42, 16777215);

        int posX = MathHelper.floor_double(this.mc.thePlayer.posX);
        int posY = MathHelper.floor_double(this.mc.thePlayer.posY);
        int posZ = MathHelper.floor_double(this.mc.thePlayer.posZ);
        fontRendererObj.drawStringWithShadow(String.format("x: %.5f (%d) // c: %d (%d)", this.mc.thePlayer.posX, posX, posX >> 4, posX & 15), 2, 64, 14737632);
        fontRendererObj.drawStringWithShadow(String.format("y: %.3f (feet pos, %.3f eyes pos)", this.mc.thePlayer.getEntityBoundingBox().minY, this.mc.thePlayer.posY), 2, 72, 14737632);
        Entity entity = this.mc.getRenderViewEntity();
        EnumFacing enumfacing = entity.getHorizontalFacing();
        fontRendererObj.drawStringWithShadow(String.format("z: %.5f (%d) // c: %d (%d)", this.mc.thePlayer.posZ, posZ, posZ >> 4, posZ & 15), 2, 80, 14737632);
        int yaw = MathHelper.floor_double((double) (this.mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        fontRendererObj.drawStringWithShadow("f: " + yaw + " (" + enumfacing + ") / " + MathHelper.wrapAngleTo180_float(this.mc.thePlayer.rotationYaw), 2, 88, 14737632);
        if (this.mc.theWorld != null && !this.mc.theWorld.isAirBlock(new BlockPos(posX, posY, posZ))) {
            Chunk chunk = this.mc.theWorld.getChunkFromBlockCoords(new BlockPos(posX, posY, posZ));
            fontRendererObj.drawStringWithShadow("lc: " + (chunk.getTopFilledSegment() + 15) + " b: " + chunk.getBiome(new BlockPos(posX & 15, 64, posZ & 15), this.mc.theWorld.getWorldChunkManager()).biomeName + " bl: " + chunk.getBlockLightOpacity(new BlockPos(posX & 15, posY, posZ & 15)) + " sl: " + chunk.getBlockLightOpacity(new BlockPos(posX & 15, posY, posZ & 15)) + " rl: " + chunk.getBlockLightOpacity(new BlockPos(posX & 15, posY, posZ & 15)), 2, 96, 14737632);
        }
        fontRendererObj.drawStringWithShadow(String.format("ws: %.3f, fs: %.3f, g: %b, fl: %d", this.mc.thePlayer.capabilities.getWalkSpeed(), this.mc.thePlayer.capabilities.getFlySpeed(), this.mc.thePlayer.onGround, this.mc.theWorld.getHeight()), 2, 104, 14737632);
        if (this.mc.entityRenderer != null && this.mc.entityRenderer.isShaderActive()) {
            fontRendererObj.drawStringWithShadow(String.format("shader: %s", this.mc.entityRenderer.getShaderGroup().getShaderGroupName()), 2, 112, 14737632);
        }
    }

    protected void renderOldDebugInfoRight(ScaledResolution scaledResolution) {
        int scaledWidth = scaledResolution.getScaledWidth();
        FontRenderer fontRendererObj = this.mc.fontRendererObj;
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;
        String memoryStr = "Used memory: " + usedMemory * 100L / maxMemory + "% (" + usedMemory / 1024L / 1024L + "MB) of " + maxMemory / 1024L / 1024L + "MB";
        fontRendererObj.drawStringWithShadow(memoryStr, scaledWidth - fontRendererObj.getStringWidth(memoryStr) - 2, 2, 14737632);
        memoryStr = "Allocated memory: " + totalMemory * 100L / maxMemory + "% (" + totalMemory / 1024L / 1024L + "MB)";
        fontRendererObj.drawStringWithShadow(memoryStr, scaledWidth - fontRendererObj.getStringWidth(memoryStr) - 2, 12, 14737632);
    }
}
