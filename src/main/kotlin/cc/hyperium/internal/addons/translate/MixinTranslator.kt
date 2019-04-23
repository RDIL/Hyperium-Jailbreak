package cc.hyperium.internal.addons.translate

import cc.hyperium.internal.addons.AddonManifest
import cc.hyperium.internal.addons.AbstractTranslator
import org.spongepowered.asm.mixin.Mixins

class MixinTranslator : AbstractTranslator() {
    override fun translate(manifest: AddonManifest) {
        manifest.mixinConfigs?.forEach(Mixins::addConfiguration)
    }
}
