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

public class AddonBootstrap {
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

    public void init() throws IOException {
        if (phase != Phase.NOT_STARTED) {
            throw new IOException("Cannot initialise bootstrap twice");
        }

        phase = Phase.PREINIT;
        Launch.classLoader.addClassLoaderExclusion("cc.hyperium.internal.addons.AddonBootstrap");
        Launch.classLoader.addClassLoaderExclusion("cc.hyperium.internal.addons.AddonManifest");
        Launch.classLoader.addClassLoaderExclusion("me.kbrewster.blazeapi.internal.addons.translate.");

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
                System.out.println("Loading " + file.getName());
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
        PREINIT
        ,
        INIT,
        DEFAULT
    }
}
