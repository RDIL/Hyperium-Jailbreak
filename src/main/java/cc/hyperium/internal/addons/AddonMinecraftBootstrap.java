package cc.hyperium.internal.addons;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddonMinecraftBootstrap {
    private static final List<IAddon> LOADED_ADDONS = new ArrayList<>();

    public static void init() throws IOException {
        final List<AddonManifest> toLoad = new ArrayList<>(AddonBootstrap.INSTANCE.getAddonManifests());

        final List<IAddon> loaded = new ArrayList<>();

        for (AddonManifest addon : toLoad) {
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
