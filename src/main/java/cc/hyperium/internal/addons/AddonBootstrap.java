package cc.hyperium.internal.addons;

import cc.hyperium.internal.addons.misc.AddonManifestParser;
import cc.hyperium.internal.addons.strategy.AddonLoaderStrategy;
import cc.hyperium.internal.addons.strategy.DefaultAddonLoader;
import cc.hyperium.internal.addons.strategy.WorkspaceAddonLoader;
import cc.hyperium.internal.addons.translate.ITranslator;
import cc.hyperium.internal.addons.translate.MixinTranslator;
import cc.hyperium.internal.addons.translate.TransformerTranslator;
import net.minecraft.launchwrapper.Launch;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarFile;

public class AddonBootstrap {
    private static ArrayList<File> addonResourcePack = new ArrayList<>();
    public static AddonBootstrap INSTANCE = new AddonBootstrap();
    private ArrayList<File> jars;
    private ArrayList<AddonManifest> addonManifests = new ArrayList<>();
    private ArrayList<AddonManifest> pendingManifests = new ArrayList<>();
    private File modDirectory = new File("addons");
    private File pendingDirectory = new File("pending-addons");
    private Phase phase = Phase.NOT_STARTED;
    private DefaultAddonLoader loader = new DefaultAddonLoader();
    private WorkspaceAddonLoader workspaceAddonLoader = new WorkspaceAddonLoader();
    private List<ITranslator> translators = Arrays.asList(
            new MixinTranslator(),
            new TransformerTranslator()
    );

    private AddonBootstrap() {
        if (!modDirectory.mkdirs() && !modDirectory.exists()) {
            try {
                throw new IOException("Unable to create Addon Directory!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FilenameFilter filenameFilter = (dir, name) -> name.toLowerCase().endsWith(".jar");

        jars = (ArrayList<File>) Arrays.asList(Objects.requireNonNull(modDirectory.listFiles(filenameFilter)));
    }

    public void init() {
        if (phase != Phase.NOT_STARTED) {
            try {
                throw new IOException("Cannot initialize bootstrap twice");
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    }

    private List<? extends AddonManifest> loadAddons(AddonLoaderStrategy loader) {
        ArrayList<AddonManifest> addons = new ArrayList<>();
        File[] pendings = pendingDirectory.exists() ? pendingDirectory.listFiles() : new File[0];

        try {
            if (pendingDirectory.exists() && pendings != null) {
                for (File pending : pendings) {
                    pendingManifests.add((new AddonManifestParser(new JarFile(pending))).getAddonManifest());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Iterator<File> it = jars.iterator();

        File jar;
        AddonManifest addon;
        while (it.hasNext()) {
            jar = it.next();

            try {
                addon = loadAddon(loader, jar);
                if (addon != null) {
                    addons.add(addon);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return addons;
    }

    private AddonManifest loadWorkspaceAddon() {
        AddonManifest manifest = new AddonManifest();
        try {
            loadAddon(workspaceAddonLoader, null);
        } catch (Exception e) {
            e.printStackTrace();
            manifest = null;
        }
        return manifest;
    }

    private AddonManifest loadAddon(AddonLoaderStrategy loader, File addon) throws Exception {
        return loader.load(addon);
    }

    public Phase getPhase() {
        return phase;
    }

    public ArrayList<AddonManifest> getAddonManifests() {
        return addonManifests;
    }

    public ArrayList<AddonManifest> getPendingManifests() {
        return pendingManifests;
    }

    public static ArrayList<File> getAddonResourcePack() {
        return addonResourcePack;
    }

    public ArrayList<File> getJars() {
        return jars;
    }

    public DefaultAddonLoader getLoader() {
        return loader;
    }

    public File getModDirectory() {
        return modDirectory;
    }

    public File getPendingDirectory() {
        return pendingDirectory;
    }

    public List<ITranslator> getTranslators() {
        return translators;
    }

    public WorkspaceAddonLoader getWorkspaceAddonLoader() {
        return workspaceAddonLoader;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public static ArrayList<File> getAddonResourcePacks() {
        return addonResourcePack;
    }
}
