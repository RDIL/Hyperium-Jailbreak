package cc.hyperium.mixinsimp.entity;

import org.lwjgl.opengl.GL11;

public class HyperiumRenderFish {
    public HyperiumRenderFish() {}

    public void doRender() {
        // Set line width to normal to prevent becoming thick.
        GL11.glLineWidth(1.0F);
    }
}
