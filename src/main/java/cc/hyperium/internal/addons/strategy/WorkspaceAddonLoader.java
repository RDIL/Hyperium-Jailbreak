package cc.hyperium.internal.addons.strategy;

import java.io.File;
import java.nio.charset.Charset;
import cc.hyperium.internal.addons.AddonBootstrap;
import cc.hyperium.internal.addons.AddonManifest;
import cc.hyperium.internal.addons.misc.AddonManifestParser;
import org.apache.commons.io.IOUtils;
import java.util.Objects;

/**
 * The addon loader for development environments.
 */
public class WorkspaceAddonLoader extends AddonLoaderStrategy {
    @Override
    public AddonManifest load(File file) throws Exception {
        if (this.getClass().getClassLoader().getResource("addon.json") != null) {
            if (this.getClass().getClassLoader().getResource("pack.mcmeta") != null) {
                AddonBootstrap.INSTANCE.getAddonResourcePacks().add(file);
            }

            String lines = IOUtils.toString(Objects.requireNonNull(this.getClass().getClassLoader().getResource("addon.json")).openStream(), Charset.defaultCharset());
            return new AddonManifestParser(lines).getAddonManifest();
        } else {
            return null;
        }
    }
}
