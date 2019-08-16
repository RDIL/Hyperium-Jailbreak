package cc.hyperium.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public enum Icons {
    SETTINGS(new ResourceLocation("textures/material/settings.png")),
    EXTENSION(new ResourceLocation("textures/material/extension.png")),
    EXIT(new ResourceLocation("textures/material/exit.png")),
    ARROW_DOWN_ALT(new ResourceLocation("textures/material/arrow_down_alt.png")),
    ARROW_UP_ALT(new ResourceLocation("textures/material/arrow_up_alt.png")),
    ARROW_LEFT(new ResourceLocation("textures/material/arrow_left.png")),
    ARROW_RIGHT(new ResourceLocation("textures/material/arrow_right.png"));

    private ResourceLocation res;
    Icons(ResourceLocation res) {
        this.res = res;
    }
    public void bind() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(res);
    }
}
