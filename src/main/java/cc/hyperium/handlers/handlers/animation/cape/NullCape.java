package cc.hyperium.handlers.handlers.animation.cape;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class NullCape implements ICape {
    public static final NullCape INSTANCE = new NullCape();

    @Override
    public ResourceLocation get() {
        return null;
    }

    @Override
    public void delete(TextureManager manager) {}
}
