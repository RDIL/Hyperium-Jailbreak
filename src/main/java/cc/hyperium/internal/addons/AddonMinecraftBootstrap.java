package cc.hyperium.internal.addons;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddonMinecraftBootstrap {
    private final List<IAddon> LOADED_ADDONS = new ArrayList<>();

    public static AddonMinecraftBootstrap INSTANCE = new AddonMinecraftBootstrap();

    public void init() {
        try {
            if (AddonBootstrap.INSTANCE.getPhase() != AddonBootstrap.Phase.INIT) {
                throw new IOException("Bootstrap currently at AddonBoostrap.Phase." + AddonBootstrap.INSTANCE.phase + ", it should be at INIT");
            }

            List<AddonManifest> toLoad = new ArrayList<>(AddonBootstrap.INSTANCE.getAddonManifests());

            List<IAddon> loaded = new ArrayList<>();

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
            AddonBootstrap.INSTANCE.setPhase(AddonBootstrap.Phase.DEFAULT);
        } catch (IOException ignored) {

        }
    }

    public List<IAddon> getLoadedAddons() {
        return LOADED_ADDONS;
    }
}
