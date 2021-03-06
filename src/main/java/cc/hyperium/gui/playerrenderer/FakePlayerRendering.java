package cc.hyperium.gui.playerrenderer;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.client.renderer.GlStateManager;

public class FakePlayerRendering {
    private final Renderer normalArms;
    private final Renderer smallArms;
    private final AbstractClientPlayer player;

    public FakePlayerRendering(GameProfile profile) {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        normalArms = new Renderer(renderManager, false);
        smallArms = new Renderer(renderManager, true);
        boolean isSessionProfile = Minecraft.getMinecraft().getSession().getProfile() == profile;
        if (!isSessionProfile) profile = Minecraft.getMinecraft().getSessionService().fillProfileProperties(profile, true);
        player = new FakePlayer(profile);
    }

    public void renderPlayerModel(int posX, int posY, float scale, float rotation) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.getRenderManager().pointedEntity = player;
        mc.getRenderManager().renderEngine = mc.getTextureManager();

        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, 50.0F);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);

        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();

        RenderHelper.enableStandardItemLighting();

        GlStateManager.rotate(rotation, 0.0F, 1.0F, 0.0F);
        player.rotationYawHead = player.rotationYaw + rotation;

        GlStateManager.translate(0.0F, player.getYOffset(), 0.0F);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);

        Renderer renderer = player.getSkinType().equals("slim") ? smallArms : normalArms;
        renderer.doRender(player, 0.0D, 0.0D, 0.0F, 0.0F, 0.625F);

        GlStateManager.disableDepth();
        GlStateManager.disableAlpha();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private static final class Renderer extends RenderPlayer {
        public Renderer(RenderManager renderManager, boolean useSmallArms) {
            super(renderManager, useSmallArms);
        }

        @Override
        protected boolean canRenderName(AbstractClientPlayer targetEntity) {
            return false;
        }
    }

    private static final class FakePlayer extends AbstractClientPlayer {
        private final NetworkPlayerInfo playerInfo;

        FakePlayer(GameProfile profile) {
            super(new FakeWorld(), profile);
            playerInfo = new NetworkPlayerInfo(profile);
            playerInfo.getLocationSkin();
        }

        @Override
        protected NetworkPlayerInfo getPlayerInfo() {
            return playerInfo;
        }

        @Override
        public boolean isSpectator() {
            return false;
        }

        @Override
        public float getBrightness(float p_70013_1_) {
            return 0;
        }
    }

    private static final class FakeWorld extends World {
        FakeWorld() {
            super(null, null, new FakeWorldProvider(), null, true);
        }

        @Override
        protected IChunkProvider createChunkProvider() {
            return null;
        }

        @Override
        protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
            return false;
        }

        @Override
        protected int getRenderDistanceChunks() {
            return 0;
        }

        @Override
        public BlockPos getSpawnPoint() {
            return new BlockPos(0, 0, 0);
        }
    }

    private static final class FakeWorldProvider extends WorldProvider {
        FakeWorldProvider() {}
        @Override
        public String getDimensionName() {
            return "Overworld";
        }

        @Override
        public String getInternalNameSuffix() {
            return "";
        }
    }
}
