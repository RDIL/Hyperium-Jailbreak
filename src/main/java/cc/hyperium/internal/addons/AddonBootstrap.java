package cc.hyperium.internal.addons;

import cc.hyperium.Hyperium;
import cc.hyperium.internal.addons.misc.AddonManifestParser;
import cc.hyperium.internal.addons.strategy.AddonLoaderStrategy;
import cc.hyperium.internal.addons.strategy.DefaultAddonLoader;
import cc.hyperium.internal.addons.strategy.WorkspaceAddonLoader;
import cc.hyperium.internal.addons.translate.ITranslator;
import cc.hyperium.internal.addons.translate.MixinTranslator;
import cc.hyperium.internal.addons.translate.TransformerTranslator;
import net.minecraft.launchwrapper.Launch;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarFile;

/**
 * The addon system core.
 */
public class AddonBootstrap {
    /**
     * The instance.
     */
    public static AddonBootstrap INSTANCE = new AddonBootstrap();
    private static final List<File> addonResourcePacks = new ArrayList<>();

    private final File MOD_DIRECTORY = new File("addons");
    private final File PENDING_DIRECTORY = new File("pending-addons");
    private final DefaultAddonLoader loader = new DefaultAddonLoader();
    private final WorkspaceAddonLoader workspaceLoader = new WorkspaceAddonLoader();
    private final List<File> jars;
    private final List<ITranslator> translators = Arrays.asList(new MixinTranslator(), new TransformerTranslator());
    private final List<AddonManifest> addonManifests = new ArrayList<>();
    private final List<AddonManifest> pendingManifests = new ArrayList<>();

    /**
     * The {@link cc.hyperium.internal.addons.AddonBootstrap.Phase} the system is currently at.
     */
    public Phase phase = Phase.NOT_STARTED;

    /**
     * Creates a new instance of the bootstrap system, and locates addon jar files.
     */
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
        List<File> filteredJars = new ArrayList<>();
        File[] files = MOD_DIRECTORY.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().toLowerCase().endsWith(".jar")) {
                    filteredJars.add(file);
                }
            }
        }

        jars = filteredJars;
    }

    /**
     * Run the system's main functionality.
     *
     * @throws IOException If bootstrap is initialized more then once.
     */
    public void init() throws IOException {
        if (phase != Phase.NOT_STARTED) {
            throw new IOException("Cannot initialise bootstrap twice");
        }

        phase = Phase.PREINIT;
        Launch.classLoader.addClassLoaderExclusion("cc.hyperium.internal.addons.AddonBootstrap");
        Launch.classLoader.addClassLoaderExclusion("cc.hyperium.internal.addons.AddonManifest");

        AddonManifest workspaceAddon = loadWorkspaceAddon();

        if (workspaceAddon != null) {
            addonManifests.add(workspaceAddon);
        }
        addonManifests.addAll(loadAddons(loader));

        for (AddonManifest manifest : addonManifests) {
            for (ITranslator translator : translators) {
                translator.translate(manifest);
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
                AddonManifest addon = loadAddon(loader, file);
                Hyperium.LOGGER.debug("Loading " + file.getName());
                if (addon == null) {
                    continue;
                }
                addons.add(addon);
            } catch (Exception e) {
                Hyperium.LOGGER.error("Could not load {}!", file.getName());
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
                    Hyperium.LOGGER.info("Failed to move addon: {}", file.getName());
                    e.printStackTrace();
                }

                try {
                    AddonManifest addon = loadAddon(loader, dest);
                    if (addon == null) {
                        continue;
                    }
                    addons.add(addon);
                } catch (Exception e) {
                    Hyperium.LOGGER.error("Could not load {}!", dest.getName());
                    e.printStackTrace();
                }
            }
        }

        return addons;
    }

    private AddonManifest loadWorkspaceAddon() {
        try {
            return loadAddon(workspaceLoader, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private AddonManifest loadAddon(AddonLoaderStrategy loader, File addon) throws Exception {
        return loader.load(addon);
    }

    /**
     * Get a {@link java.util.List} of the addon resource packs.
     *
     * @return The addon resource packs.
     */
    public List<File> getAddonResourcePacks() {
        return addonResourcePacks;
    }

    /**
     * Get a {@link java.util.List} of addon manifests that need to have their main classes initialized.
     *
     * @return The pending addon manifests.
     */
    public List<AddonManifest> getPendingManifests() {
        return pendingManifests;
    }

    /**
     * Get the current {@link cc.hyperium.internal.addons.AddonBootstrap.Phase} the system is at.
     *
     * @return The current phase.
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * Set the current phase to a new phase.
     *
     * @param phase The new phase.
     */
    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    /**
     * Get a {@link java.util.List} of addon manifests.
     *
     * @return The addon manifests.
     */
    public List<AddonManifest> getAddonManifests() {
        return addonManifests;
    }

    /**
     * The different phases the system can be at.
     */
    public enum Phase {
        /**
         * The system hasn't started.
         */
        NOT_STARTED,
        /**
         * The system is pre-initializing.
         */
        PREINIT,
        /**
         * The system is running its main operations.
         */
        INIT,
        /**
         * The system is done.
         */
        DEFAULT
    }
}
