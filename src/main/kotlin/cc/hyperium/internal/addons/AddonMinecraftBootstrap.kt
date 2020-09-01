package cc.hyperium.internal.addons

import java.io.IOException

object AddonMinecraftBootstrap {
    @JvmStatic
    val LOADED_ADDONS = ArrayList<IAddon>()
        @JvmName("getLoadedAddons") get

    @JvmStatic
    fun init() {
        try {
            if (AddonBootstrap.phase != AddonBootstrap.Phase.INIT) {
                throw IOException("Bootstrap currently at AddonBoostrap.Phase.${AddonBootstrap.phase}, it should be at INIT")
            }

            val toLoadMap = AddonBootstrap.addonManifests.map { it.name to it }.toMap().toMutableMap()

            val toLoad = toLoadMap.map { it.value }.toMutableList()

            val loaded = ArrayList<IAddon>()
            for (addon in toLoad) {
                try {
                    val o = Class.forName(addon.mainClass).newInstance()
                    if (o is IAddon) {
                        loaded.add(o)
                    } else {
                        throw IOException("Main class isn't instance of IAddon")
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            LOADED_ADDONS.addAll(loaded)
            LOADED_ADDONS.forEach(IAddon::onLoad)
            AddonBootstrap.phase = AddonBootstrap.Phase.DEFAULT
        } catch (ignored: Exception) {}
    }
}
