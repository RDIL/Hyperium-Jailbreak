package cc.hyperium.handlers.handlers.animation.cape;

import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class StaticCape implements ICape {
    private ResourceLocation location;

    StaticCape(ResourceLocation location) {
        this.location = location;
    }

    @Override
    public ResourceLocation get() {
        return location;
    }

    @Override
    public void delete(TextureManager manager) {
        ITextureObject texture = manager.getTexture(location);
        if (texture instanceof ThreadDownloadImageData) {
            ((ThreadDownloadImageData) texture).setBufferedImage(null);
        }
        manager.deleteTexture(location);
    }
}
