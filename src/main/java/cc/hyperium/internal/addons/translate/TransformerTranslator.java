package cc.hyperium.internal.addons.translate;

import cc.hyperium.internal.addons.AddonManifest;
import net.minecraft.launchwrapper.Launch;

/**
 * Translator that adds the {@code transformerClass} to the {@link net.minecraft.launchwrapper.LaunchClassLoader} instance.
 */
public class TransformerTranslator implements ITranslator {
    @Override
    public void translate(AddonManifest manifest) {
        if (manifest.getTransformerClass() != null) {
            Launch.classLoader.registerTransformer(manifest.getTransformerClass());
        }
    }
}
