package cc.hyperium.internal.addons;

import cc.hyperium.internal.addons.misc.AddonManifestParser;
import cc.hyperium.internal.addons.strategy.AddonLoaderStrategy;
import cc.hyperium.internal.addons.strategy.DefaultAddonLoader;
import cc.hyperium.internal.addons.strategy.WorkspaceAddonLoader;
import cc.hyperium.launch.HyperiumTweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

public class AddonBootstrap {
    public static AddonBootstrap INSTANCE = new AddonBootstrap();
    public static final Logger LOGGER = LogManager.getLogger();
    private static final List<File> addonResourcePacks = new ArrayList<>();

    private static final File MOD_DIRECTORY = new File("addons");
    private static final File PENDING_DIRECTORY = new File("pending-addons");
    private final DefaultAddonLoader loader = new DefaultAddonLoader();
    private final WorkspaceAddonLoader workspaceLoader = new WorkspaceAddonLoader();
    private final List<File> jars;
    private final List<AddonManifest> addonManifests = new ArrayList<>();
    private final List<AddonManifest> pendingManifests = new ArrayList<>();

    public Phase phase = Phase.NOT_STARTED;

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

        final List<File> filteredJars = new ArrayList<>();
        final File[] files = MOD_DIRECTORY.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().toLowerCase().endsWith(".jar")) {
                    filteredJars.add(file);
                }
            }
        }

        jars = filteredJars;
    }

    public void init(LaunchClassLoader classLoader, HyperiumTweaker primaryTweaker) throws IOException {
        if (phase != Phase.NOT_STARTED) {
            throw new IOException("Cannot initialise bootstrap twice");
        }

        phase = Phase.PREINIT;
        Launch.classLoader.addClassLoaderExclusion("cc.hyperium.internal.addons.AddonBootstrap");
        Launch.classLoader.addClassLoaderExclusion("cc.hyperium.internal.addons.AddonManifest");

        AddonManifest workspaceAddon = loadWorkspaceAddon();

        if (workspaceAddon != null) {
            LOGGER.info("WorkspaceAddonLoader check passed.");
            addonManifests.add(workspaceAddon);
        }
        addonManifests.addAll(loadAddons(loader));

        for (AddonManifest manifest : addonManifests) {
            final List<?> mixinConfigs = manifest.getMixinConfigs();

            if (mixinConfigs != null) {
                for (Object o : mixinConfigs) {
                    final String p1 = (String) o;
                    Mixins.addConfiguration(p1);
                    LOGGER.info("Addon " + manifest.getName() + " registered mixin configuration " + p1);
                }
            }

            if (manifest.getTransformerClass() != null) {
                classLoader.registerTransformer(manifest.getTransformerClass());
                LOGGER.info("Addon " + manifest.getName() + " registered transformer " + manifest.getTransformerClass());
            }

            if (manifest.getTweakerClass() != null) {
                primaryTweaker.injectCascadingTweak(manifest.getTweakerClass());
                LOGGER.info("Addon " + manifest.getName() + " registered cascading tweak class " + manifest.getTweakerClass());
            }
        }

        phase = Phase.INIT;
    }

    private List<AddonManifest> loadAddons(AddonLoaderStrategy loader) {
        List<AddonManifest> addons = new ArrayList<>();
        File[] pendings = new File[0];
        if (PENDING_DIRECTORY.exists()) pendings = PENDING_DIRECTORY.listFiles();

        try {
            if (PENDING_DIRECTORY.exists()) {
                if (pendings != null) {
                    for (File file : pendings) {
                        pendingManifests.add(new AddonManifestParser(new JarFile(file)).getAddonManifest());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (File file : jars) {
            try {
                AddonManifest addon = loader.load(file);
                LOGGER.info("Loading addon " + file.getName());
                if (addon == null) {
                    continue;
                }
                addons.add(addon);
            } catch (Exception e) {
                LOGGER.error("Could not load addon {}!", file.getName());
                e.printStackTrace();
            }
        }

        pendingManifests.clear();
        if (pendings != null) {
            for (File file : pendings) {
                File dest = new File(MOD_DIRECTORY, file.getName());
                try {
                    FileUtils.moveFile(file, dest);
                } catch (IOException e) {
                    LOGGER.info("Failed to move addon: {}", file.getName());
                    e.printStackTrace();
                }

                try {
                    AddonManifest addon = loader.load(dest);
                    if (addon == null) {
                        continue;
                    }
                    addons.add(addon);
                } catch (Exception e) {
                    LOGGER.error("Could not load {}!", dest.getName());
                    e.printStackTrace();
                }
            }
        }

        return addons;
    }


    private AddonManifest loadWorkspaceAddon() {
        try {
            return workspaceLoader.load(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<File> getAddonResourcePacks() {
        return addonResourcePacks;
    }

    public List<AddonManifest> getPendingManifests() {
        return pendingManifests;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public List<AddonManifest> getAddonManifests() {
        return addonManifests;
    }

    public enum Phase {
        NOT_STARTED,
        PREINIT,
        INIT,
        DEFAULT
    }
}
