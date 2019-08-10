package cc.hyperium.internal.addons.strategy

import cc.hyperium.internal.addons.AddonBootstrap
import cc.hyperium.internal.addons.AddonManifest
import cc.hyperium.internal.addons.misc.AddonManifestParser
import net.minecraft.launchwrapper.Launch
import java.io.File
import java.io.IOException
import java.util.jar.JarFile

class DefaultAddonLoader : AddonLoaderStrategy() {
    @Throws(Exception::class)
    override fun load(file: File?): AddonManifest? {
        if (file == null) throw IOException("Could not load file; parameter issued was null.")
        val jar = JarFile(file)
        if (jar.getJarEntry("pack.mcmeta") != null) AddonBootstrap.addonResourcePacks.add(file)
        val manifest = AddonManifestParser(jar).getAddonManifest()
        if (
            arrayOf("AutoFriend", "Custom Crosshair Addon", "Tab Toggle", "SidebarAddon", "BossbarAddon", "FortniteCompassMod", "Item Physic")
                .contains(manifest.name) || AddonBootstrap.pendingManifests.stream().anyMatch { it.name.equals(manifest.name) }
        ) {
            file.delete()
            return null
        }
        val uri = file.toURI()
        Launch.classLoader.addURL(uri.toURL())
        return manifest
    }
}
