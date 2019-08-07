package cc.hyperium.mixins.client.particle;

import cc.hyperium.config.Settings;
import cc.hyperium.mixinsimp.renderer.client.particle.IMixinEffectRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityParticleEmitter;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

@Mixin(EffectRenderer.class)
public abstract class MixinEffectRenderer implements IMixinEffectRenderer {
    @Shadow
    @Final
    private static ResourceLocation particleTextures;
    @Shadow
    protected World worldObj;
    @Shadow
    private Map<Integer, IParticleFactory> particleTypes;
    // its not happy about this but we can't do better because Minecraft
    private ConcurrentLinkedQueue<EntityFX>[][] modifiedFxLayer = new ConcurrentLinkedQueue[4][];
    private ConcurrentLinkedQueue<EntityParticleEmitter> modifiedParticlEmmiters = new ConcurrentLinkedQueue<>();
    @Shadow
    private TextureManager renderer;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void load(World in, TextureManager manager, CallbackInfo info) {
        for (int i = 0; i < 4; ++i) {
            this.modifiedFxLayer[i] = new ConcurrentLinkedQueue[2];

            for (int j = 0; j < 2; ++j) {
                this.modifiedFxLayer[i][j] = new ConcurrentLinkedQueue<>();
            }
        }
    }

    @Overwrite
    private void tickParticle(EntityFX p_178923_1_) {
        if (p_178923_1_ == null)
            return;
        try {
            p_178923_1_.onUpdate();
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking Particle");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being ticked");
            final int i = p_178923_1_.getFXLayer();
            crashreportcategory.addCrashSectionCallable("Particle", p_178923_1_::toString);
            crashreportcategory.addCrashSectionCallable("Particle Type", () -> i == 0 ? "MISC_TEXTURE" : (i == 1 ? "TERRAIN_TEXTURE" : (i == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + i)));
            ReportedException reportedException = new ReportedException(crashreport);
            Minecraft.getMinecraft().crashed(crashreport);
            throw reportedException;
        }
    }

    @Overwrite
    private void updateEffectLayer(int p_178922_1_) {
        for (int i = 0; i < 2; ++i) {
            int finalI = i;
            this.updateEffectAlphaLayer(this.modifiedFxLayer[p_178922_1_][finalI]);
        }
    }

    private void updateEffectAlphaLayer(ConcurrentLinkedQueue<EntityFX> queue) {
        queue.forEach(this::tickParticle);
        queue.removeIf(entityFX -> entityFX.isDead);
    }

    @Overwrite
    public void emitParticleAtEntity(Entity entityIn, EnumParticleTypes particleTypes) {
        this.modifiedParticlEmmiters.add(new EntityParticleEmitter(this.worldObj, entityIn, particleTypes));
    }

    @Overwrite
    public void addEffect(EntityFX effect) {
        int i = effect.getFXLayer();
        int j = effect.getAlpha() != 1.0F ? 0 : 1;

        if (this.modifiedFxLayer[i][j].size() >= Settings.MAX_WORLD_PARTICLES_INT) {
            this.modifiedFxLayer[i][j].poll();
        }

        this.modifiedFxLayer[i][j].add(effect);
    }

    @Overwrite
    public void renderLitParticles(Entity entityIn, float p_78872_2_) {
        float f1 = MathHelper.cos(entityIn.rotationYaw * 0.017453292F);
        float f2 = MathHelper.sin(entityIn.rotationYaw * 0.017453292F);
        float f3 = -f2 * MathHelper.sin(entityIn.rotationPitch * 0.017453292F);
        float f4 = f1 * MathHelper.sin(entityIn.rotationPitch * 0.017453292F);
        float f5 = MathHelper.cos(entityIn.rotationPitch * 0.017453292F);

        for (int i = 0; i < 2; ++i) {
            ConcurrentLinkedQueue<EntityFX> queue = this.modifiedFxLayer[3][i];

            if (!queue.isEmpty()) {
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();

                queue.forEach(entityFX -> entityFX.renderParticle(worldrenderer, entityIn, p_78872_2_, f1, f5, f2, f3, f4));
            }
        }
    }

    @Overwrite
    public void clearEffects(World worldIn) {
        this.worldObj = worldIn;

        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 2; ++j) {
                this.modifiedFxLayer[i][j].clear();
            }
        }

        this.modifiedParticlEmmiters.clear();
    }

    @Overwrite
    public String getStatistics() {
        int i = 0;

        for (int j = 0; j < 4; ++j) {
            for (int k = 0; k < 2; ++k) {
                i += this.modifiedFxLayer[j][k].size();
            }
        }
        return "" + i;
    }

    @Overwrite
    public void updateEffects() {
        CountDownLatch latch = null;

        for (int i = 0; i < 4; ++i) {
            this.updateEffectLayer(i);
        }
        if (latch != null)
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        this.modifiedParticlEmmiters.forEach(EntityParticleEmitter::onUpdate);
        modifiedParticlEmmiters.removeIf(entityParticleEmitter -> entityParticleEmitter.isDead);
    }

    @Overwrite
    public void renderParticles(Entity entityIn, float partialTicks) {
        float f = ActiveRenderInfo.getRotationX();
        float f1 = ActiveRenderInfo.getRotationZ();
        float f2 = ActiveRenderInfo.getRotationYZ();
        float f3 = ActiveRenderInfo.getRotationXY();
        float f4 = ActiveRenderInfo.getRotationXZ();
        EntityFX.interpPosX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double) partialTicks;
        EntityFX.interpPosY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double) partialTicks;
        EntityFX.interpPosZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double) partialTicks;
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.alphaFunc(516, 0.003921569F);

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 2; ++j) {
                final int i_f = i;

                ConcurrentLinkedQueue<EntityFX> entityFXES = this.modifiedFxLayer[i][j];
                if (!entityFXES.isEmpty()) {
                    if(j == 0) {
                        GlStateManager.depthMask(false);
                    }
                    if(j == 1) {
                        GlStateManager.depthMask(true);
                    }

                    if(i == 0) {
                        this.renderer.bindTexture(particleTextures);
                    }
                    if(i == 1) {
                        this.renderer.bindTexture(TextureMap.locationBlocksTexture);
                    }

                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

                    for (EntityFX entityfx : entityFXES) {
                        try {
                            if (entityfx == null)
                                continue;
                            entityfx.renderParticle(worldrenderer, entityIn, partialTicks, f, f4, f1, f2, f3);
                        } catch (Throwable throwable) {
                            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Particle");
                            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being rendered");
                            crashreportcategory.addCrashSectionCallable("Particle", entityfx::toString);
                            crashreportcategory.addCrashSectionCallable("Particle Type", () -> i_f == 0 ? "MISC_TEXTURE" : (i_f == 1 ? "TERRAIN_TEXTURE" : (i_f == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + i_f)));
                            throw new ReportedException(crashreport);
                        }
                    }
                    tessellator.draw();
                }
            }
        }

        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1F);
    }

    @Override
    public Map<Integer, IParticleFactory> getParticleMap() {
        return particleTypes;
    }
}
