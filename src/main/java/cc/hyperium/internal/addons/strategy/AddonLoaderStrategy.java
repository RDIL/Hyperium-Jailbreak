package cc.hyperium.internal.addons.strategy;

import cc.hyperium.internal.addons.AddonManifest;
import java.io.File;

/**
 * An abstract addon loading strategy.
 */
public abstract class AddonLoaderStrategy {
    /**
     * Load the specified addon.
     *
     * @param var1 The addon to load.
     * @return The manifest of the loaded addon.
     * @throws Exception If loading fails.
     */
    public abstract AddonManifest load(File var1) throws Exception;
}
