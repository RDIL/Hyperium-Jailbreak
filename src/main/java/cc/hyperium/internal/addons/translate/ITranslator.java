package cc.hyperium.internal.addons.translate;

import cc.hyperium.internal.addons.AddonManifest;

/**
 * A translator that makes changes based on the {@link cc.hyperium.internal.addons.AddonManifest}.
 */
public interface ITranslator {
    /**
     * Make the necessary translations.
     *
     * @param manifest The addon manifest.
     */
    void translate(AddonManifest manifest);
}
