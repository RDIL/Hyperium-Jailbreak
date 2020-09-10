package cc.hyperium.utils;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.UUID;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

public class Utils {
    @Deprecated public static final Utils INSTANCE = new Utils();

    public static void setCursor(ResourceLocation cursor) {
        try {
            BufferedImage image = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(cursor).getInputStream());
            int w = image.getWidth();
            int h = image.getHeight();
            int[] pixels = new int[(w * h)];
            image.getRGB(0, 0, w, h, pixels, 0, w);
            ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int pixel = pixels[(h - 1 - y) * w + x];
                    buffer.put((byte) (pixel & 0xFF));
                    buffer.put((byte) (pixel >> 8 & 0xFF));
                    buffer.put((byte) (pixel >> 16 & 0xFF));
                    buffer.put((byte) (pixel >> 24 & 0xFF));
                }
            }
            buffer.flip();
            Mouse.setNativeCursor(new Cursor(w, h, 0, h - 1, 1, buffer.asIntBuffer(), null));
        } catch (Exception ignored) {}
    }

    public static UUID dashMeUp(String uuidWithoutDashes) {
        return UUID.fromString(uuidWithoutDashes.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
    }
}
