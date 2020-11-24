package cc.hyperium.internal.addons.strategy;

import cc.hyperium.internal.addons.AddonManifest;
import java.io.File;

/**
 * A strategy for loading addons.
 */
public abstract class AddonLoaderStrategy {
    /**
     * Loads the specified addon file, and returns its manifest.
     *
     * @param var1 The addon file to load.
     * @return The addon's manifest.
     * @throws Exception If something goes wrong.
     */
    public abstract AddonManifest load(File var1) throws Exception;
}
