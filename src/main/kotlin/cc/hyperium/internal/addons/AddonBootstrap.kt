package cc.hyperium.internal.addons

import cc.hyperium.Hyperium.LOGGER
import cc.hyperium.internal.addons.misc.AddonManifestParser
import cc.hyperium.internal.addons.strategy.AddonLoaderStrategy
import cc.hyperium.internal.addons.strategy.DefaultAddonLoader
import cc.hyperium.internal.addons.strategy.WorkspaceAddonLoader
import cc.hyperium.internal.addons.translate.MixinTranslator
import cc.hyperium.internal.addons.translate.TransformerTranslator
import net.minecraft.launchwrapper.Launch
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.util.jar.JarFile

object AddonBootstrap {
    private val modDirectory = File("addons")

    private val pendingDirectory = File("pending-addons")

    var phase: Phase = Phase.NOT_STARTED

    @JvmStatic
    val addonResourcePacks: ArrayList<File?> = ArrayList()

    var jars: ArrayList<File>

    private val loader = DefaultAddonLoader()

    private val workspaceLoader = WorkspaceAddonLoader()

    private val translators = arrayListOf(
            MixinTranslator(),
            TransformerTranslator()
    )

    val addonManifests = ArrayList<AddonManifest>()

    val pendingManifests = ArrayList<AddonManifest>()

    init {
        if (!modDirectory.mkdirs() && !modDirectory.exists()) {
            throw IOException("Unable to create addon directory!")
        }

        jars = modDirectory.listFiles()!!
                .filter { it.name.toLowerCase().endsWith(".jar") }
                .toCollection(ArrayList())
    }

    fun init() {
        if (phase != Phase.NOT_STARTED) {
            throw IOException("Cannot initialise bootstrap twice")
        }

        phase = Phase.PREINIT
        Launch.classLoader.addClassLoaderExclusion("cc.hyperium.internal.addons.AddonBootstrap")
        Launch.classLoader.addClassLoaderExclusion("cc.hyperium.internal.addons.AddonManifest")
        Launch.classLoader.addClassLoaderExclusion("me.kbrewster.blazeapi.internal.addons.translate.")

        with(addonManifests) {
            val workspaceAddon = loadWorkspaceAddon()
            if (workspaceAddon != null) {
                add(workspaceAddon)
            }
            addAll(loadAddons(loader))
        }

        addonManifests.forEach { manifest ->
            translators.forEach { translator -> translator.translate(manifest) }
        }
        phase = Phase.INIT
    }

    private fun loadWorkspaceAddon(): AddonManifest? {
        return try {
            loadAddon(workspaceLoader, null)
        } catch (ignored: Exception) {
            ignored.printStackTrace()
            null
        }
    }

    private fun loadAddons(loader: AddonLoaderStrategy): List<AddonManifest> {
        val addons = ArrayList<AddonManifest>()
        val pendings = if(pendingDirectory.exists()) pendingDirectory.listFiles() else arrayOf()
        try {
            if (pendingDirectory.exists())
                pendings.forEach { pendingManifests.add(AddonManifestParser(JarFile(it)).getAddonManifest()) }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        for (jar in jars) {
            try {
                val addon = loadAddon(loader, jar) ?: continue
                addons.add(addon)
            } catch (e: Exception) {
                LOGGER.error("Could not load {}!", jar.name)
                e.printStackTrace()
            }
        }
        pendingManifests.clear()
        for(jar in pendings) {
            val dest = File(modDirectory, jar.name)
            FileUtils.moveFile(jar, dest)
            try {
                val addon = loadAddon(loader, dest) ?: continue
                addons.add(addon)
            } catch (e: Exception) {
                LOGGER.error("Could not load {}!", dest.name)
                e.printStackTrace()
            }
        }
        return addons
    }

    @Throws(Exception::class)
    private fun loadAddon(loader: AddonLoaderStrategy, addon: File?): AddonManifest? {
        return loader.load(addon)
    }

    enum class Phase {
        NOT_STARTED,
        PREINIT,
        INIT,
        DEFAULT;
    }
}
