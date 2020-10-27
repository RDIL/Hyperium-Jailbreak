package cc.hyperium.internal.addons.translate;

import cc.hyperium.internal.addons.AddonManifest;
import net.minecraft.launchwrapper.Launch;

public class TransformerTranslator implements ITranslator {
    public void translate(AddonManifest manifest) {
        if (manifest.getTransformerClass() != null) {
            Launch.classLoader.registerTransformer(manifest.getTransformerClass());
        }
    }
}
