package cc.hyperium.mixinsimp.entity;

import net.minecraft.client.renderer.entity.RenderFish;
import net.minecraft.entity.projectile.EntityFishHook;
import org.lwjgl.opengl.GL11;

public class HyperiumRenderFish {
    public HyperiumRenderFish() {}

    public void doRender() {
        // Set line width to normal to prevent becoming thick.
        GL11.glLineWidth(1.0F);
    }
}
