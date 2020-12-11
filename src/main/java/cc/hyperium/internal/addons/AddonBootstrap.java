package cc.hyperium.internal.addons;

import cc.hyperium.internal.addons.strategy.AddonLoaderStrategy;
import cc.hyperium.internal.addons.strategy.DefaultAddonLoader;
import cc.hyperium.internal.addons.strategy.WorkspaceAddonLoader;
import net.minecraft.launchwrapper.Launch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * The primary addon loading system.
 */
public class AddonBootstrap {
    public static AddonBootstrap INSTANCE = new AddonBootstrap();
    public static final Logger LOGGER = LogManager.getLogger();
    private static final List<File> addonResourcePacks = new ArrayList<>();

    private static final File MOD_DIRECTORY = new File("addons");
    private static final DefaultAddonLoader loader = new DefaultAddonLoader();
    private static final WorkspaceAddonLoader workspaceLoader = new WorkspaceAddonLoader();
    private final List<File> jars = new ArrayList<>();
    private final Map<AddonManifest, File> addons = new HashMap<>();
    private final List<AddonManifest> pendingManifests = new ArrayList<>();
    private final List<String> globalMixinConfigs = new ArrayList<>();

    public AddonBootstrap() {
        if (!MOD_DIRECTORY.exists()) {
            if (!MOD_DIRECTORY.mkdirs()) {
                try {
                    throw new IOException("Unable to create addon directory!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        final File[] files = MOD_DIRECTORY.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().toLowerCase().endsWith(".jar")) {
                    jars.add(file);
                }
            }
        }
    }

    public void init() {
        // Prevent ClassCastException - AddonBootstrap cannot be cast to AddonBootstrap (same class,
        // different class loaders)
        Launch.classLoader.addClassLoaderExclusion("cc.hyperium.internal.addons.AddonBootstrap");
        Launch.classLoader.addClassLoaderExclusion("cc.hyperium.internal.addons.AddonManifest");

        final AddonManifest workspaceAddon = loadWorkspaceAddon();

        if (workspaceAddon != null) {
            LOGGER.info("WorkspaceAddonLoader check passed.");
            addons.put(workspaceAddon, null);
        }
        addons.putAll(loadAddons(loader));

        for (Map.Entry<AddonManifest, File> itemEntry : addons.entrySet()) {
            final AddonManifest manifest = itemEntry.getKey();
            final List<?> mixinConfigs = manifest.getMixinConfigs();

            if (mixinConfigs != null) {
                for (Object o : mixinConfigs) {
                    final String p1 = (String) o;
                    globalMixinConfigs.add(p1);
                    LOGGER.info("Addon " + manifest.getName() + " registered mixin configuration " + p1);
                }
            }
        }
    }

    private Map<AddonManifest, File> loadAddons(final AddonLoaderStrategy addonLoaderStrategy) {
        final Map<AddonManifest, File> localAddons = new HashMap<>();

        for (File file : jars) {
            try {
                final AddonManifest addon = addonLoaderStrategy.load(file);
                LOGGER.info("Loading addon " + file.getName());
                if (addon == null) {
                    continue;
                }
                localAddons.putIfAbsent(addon, file);
            } catch (Exception e) {
                LOGGER.error("Could not load addon {}!", file.getName());
                e.printStackTrace();
            }
        }

        return localAddons;
    }

    public void callAddonMixinBootstrap() {
        for (String config : globalMixinConfigs) {
            Mixins.addConfiguration(config);
        }
    }

    private static AddonManifest loadWorkspaceAddon() {
        try {
            return workspaceLoader.load(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void cleanup() {
        jars.clear();
        globalMixinConfigs.clear();
        pendingManifests.clear();
    }

    public List<File> getAddonResourcePacks() {
        return addonResourcePacks;
    }

    public List<AddonManifest> getPendingManifests() {
        return pendingManifests;
    }

    public Map<AddonManifest, File> getAddons() {
        return addons;
    }
}
