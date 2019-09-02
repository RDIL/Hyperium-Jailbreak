package cc.hyperium.internal.addons

import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

object AddonMinecraftBootstrap {
    @JvmStatic
    val LOADED_ADDONS = ArrayList<IAddon>()
        @JvmName("getLoadedAddons") get

    @JvmStatic
    val MISSING_DEPENDENCIES_MAP = ConcurrentHashMap<AddonManifest, ArrayList<String>>()
        @JvmName("getMissingDependenciesMap") get

    @JvmStatic
    val DEPENDENCIES_LOOP_MAP = ConcurrentHashMap<AddonManifest, ArrayList<AddonManifest>>()
        @JvmName("getDependenciesLoopMap") get

    @JvmStatic
    fun init() {
        try {
            if (AddonBootstrap.phase != AddonBootstrap.Phase.INIT) {
                throw IOException("Bootstrap currently at Phase.${AddonBootstrap.phase}, it should be at INIT")
            }

            val toLoadMap = AddonBootstrap.addonManifests.map { it.name to it }.toMap().toMutableMap()
            val iterator = toLoadMap.iterator()

            var done = false
            while (!done) {
                done = true

                loadBeforeLoop@
                while (iterator.hasNext()) {
                    val manifest = iterator.next().value
                    val dependencies = manifest.dependencies

                    for (dependency in dependencies) {
                        val dependencyManifest = toLoadMap[dependency]
                        if (dependencyManifest == null) {
                            toLoadMap.remove(manifest.name)
                            MISSING_DEPENDENCIES_MAP.computeIfAbsent(manifest) { ArrayList() }.add(dependency.toString())
                            continue@loadBeforeLoop
                        }

                        if (dependencyManifest.dependencies.contains(manifest.name)) {
                            // The addons are depending on eachother. No way to enable them in the right order
                            iterator.remove()
                            toLoadMap.remove(manifest.name)
                            done = false
                            DEPENDENCIES_LOOP_MAP.computeIfAbsent(manifest) { ArrayList() }.add(dependencyManifest)
                            continue@loadBeforeLoop
                        }
                    }
                }
            }

            val toLoad = toLoadMap.map { it.value }.toMutableList()

            val inEdges = toLoad.map { it to HashSet<AddonManifest>() }.toMap().toMutableMap()
            val outEdges = toLoad.map { it to HashSet<AddonManifest>() }.toMap().toMutableMap()

            toLoad.forEach { manifest ->
                manifest.dependencies.forEach {
                    val dependency = toLoadMap[it]
                    if (dependency != null) {
                        outEdges[manifest]!!.add(dependency)
                        inEdges[dependency]!!.add(manifest)
                    }
                }
            }

            val toSort = HashSet<AddonManifest>()
            toLoad.forEach {
                if (inEdges[it]?.size == 0) {
                    toSort.add(it)
                }
            }

            toLoad.clear()

            // order
            while (toSort.isNotEmpty()) {
                // remove a node n from toSort
                val n = toSort.iterator().next()
                toSort.remove(n)

                // insert n into L
                toLoad.add(n)

                // for each node m with an edge e from n to m do
                val it = outEdges[n]?.iterator()
                while (it!!.hasNext()) {
                    // remove edge e from the graph
                    val m = it.next()
                    it.remove()// Remove edge from n
                    inEdges[m]!!.remove(n)// Remove edge from m

                    // if m has no other incoming edges then insert m into toSort
                    if (inEdges[m]!!.isEmpty()) {
                        toSort.add(m)
                    }
                }
            }

            // Check to see if all edges are removed
            var cycle = false
            for (n in toLoad) {
                if (!inEdges[n]?.isEmpty()!!) {
                    cycle = true
                    break
                }
            }

            if (cycle) {
                return
            }

            val dontLoad: ArrayList<AddonManifest> = ArrayList()

            for (addon in dontLoad) {
                toLoad.remove(addon)
            }

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
            AddonBootstrap.phase = AddonBoostrap.Phase.DEFAULT
        } catch (ignored: Exception) {}
    }
}
