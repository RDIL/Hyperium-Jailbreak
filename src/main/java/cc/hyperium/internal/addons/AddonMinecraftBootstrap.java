package cc.hyperium.internal.addons;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddonMinecraftBootstrap {
    private static final List<IAddon> LOADED_ADDONS = new ArrayList<>();

    public static void init() throws IOException {
        final Map<AddonManifest, File> toLoad = AddonBootstrap.INSTANCE.getAddons();

        final List<IAddon> loaded = new ArrayList<>();

        for (Map.Entry<AddonManifest, File> addonManifestFileEntry : toLoad.entrySet()) {
            final AddonManifest addon = addonManifestFileEntry.getKey();
            try {
                Object o = Class.forName(addon.getMainClass()).newInstance();
                if (o instanceof IAddon) {
                    loaded.add((IAddon) o);
                } else {
                    throw new IOException("Main class isn't instance of IAddon");
                }
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        LOADED_ADDONS.addAll(loaded);
        for (IAddon addon : LOADED_ADDONS) {
            addon.onLoad();
        }
    }

    public static List<IAddon> getLoadedAddons() {
        return LOADED_ADDONS;
    }
}
