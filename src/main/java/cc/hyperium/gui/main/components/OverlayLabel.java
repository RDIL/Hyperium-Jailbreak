package cc.hyperium.gui.main.components;

import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.gui.Gui;
import java.awt.Color;

public class OverlayLabel extends OverlayComponent {
    private final HyperiumFontRenderer fr;
    private Runnable click;

    public void mouseClicked(int mouseX, int mouseY, int overlayX, int overlayY, int w, int h) {
        if (mouseX >= overlayX && mouseX <= overlayX + w && mouseY >= overlayY && mouseY <= overlayY + h) {
            this.click.run();
        }
    }

    public boolean render(int mouseX, int mouseY, int overlayX, int overlayY, int w, int h, int overlayH) {
        if (this.getClass().equals(OverlayLabel.class)) {
            int textY = overlayY + (h - this.fr.FONT_HEIGHT) / 2;
            if (textY < overlayH / 4) {
                return false;
            }

            if (textY + h > overlayH / 4 * 3) {
                return false;
            }
            Gui.drawRect(overlayX, overlayY, overlayX + w, overlayY + h, new Color(40, 40, 40).getRGB());
        }

        return super.render(mouseX, mouseY, overlayX, overlayY, w, h, overlayH);
    }

    public OverlayLabel(String label, boolean enabled, Runnable click) {
        super(enabled);
        this.click = click;
        this.fr = new HyperiumFontRenderer("Arial", 0, 20);
        this.label = label;
    }
}
