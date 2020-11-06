package cc.hyperium.internal.addons.translate;

import cc.hyperium.internal.addons.AddonManifest;
import java.util.List;
import org.spongepowered.asm.mixin.Mixins;

/**
 * Translator that calls {@link org.spongepowered.asm.mixin.Mixins#addConfiguration(String)} on all the {@code mixinConfig}s.
 */
public class MixinTranslator implements ITranslator {
    @SuppressWarnings("rawtypes")
    @Override
    public void translate(AddonManifest manifest) {
        List configs = manifest.getMixinConfigs();
        if (configs != null) {
            for (Object o : configs) {
                String p1 = (String) o;
                Mixins.addConfiguration(p1);
            }
        }
    }
}
