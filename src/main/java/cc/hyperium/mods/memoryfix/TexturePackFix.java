package cc.hyperium.mods.memoryfix;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

@SuppressWarnings("unused")
public class TexturePackFix {
    public static final int SIZE = 64;

    public static BufferedImage scalePackImage(BufferedImage image) throws IOException {
        if (image == null) {
            return null;
        }
        BufferedImage smallImage = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = smallImage.getGraphics();
        graphics.drawImage(image, 0, 0, SIZE, SIZE, null);
        graphics.dispose();
        return smallImage;
    }
}
