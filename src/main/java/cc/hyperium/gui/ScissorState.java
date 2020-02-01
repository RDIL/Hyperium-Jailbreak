package cc.hyperium.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.BufferUtils;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;

public class ScissorState {
    private static final IntBuffer boxBuf = BufferUtils.createIntBuffer(4);

    public static void scissor(int x, int y, int width, int height, boolean useWindowCoords) {
        if (useWindowCoords) {
            int scaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();

            x *= scaleFactor;
            y *= scaleFactor;
            width *= scaleFactor;
            height *= scaleFactor;
        }

        int sx = x;
        int sy = Minecraft.getMinecraft().displayHeight - (y + height);
        int sw = width;
        int sh = height;
        GL11.glPushAttrib(GL11.GL_SCISSOR_BIT);

        if (GL11.glGetBoolean(GL11.GL_SCISSOR_TEST)) {
            boxBuf.rewind();
            GL11.glGetInteger(GL11.GL_SCISSOR_BOX, boxBuf);
            sx = Math.max(sx, boxBuf.get(0));
            sy = Math.max(sy, boxBuf.get(1));
            sw = Math.min(sw, boxBuf.get(2));
            sh = Math.min(sh, boxBuf.get(3));
        } else {
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
        }

        GL11.glScissor(sx, sy, sw, sh);
    }

    public static void endScissor() {
        GL11.glPopAttrib();
    }
}
