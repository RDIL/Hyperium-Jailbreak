package cc.hyperium.internal.addons

class AddonManifest {
    var name: String? = null

    var version: String? = null

    val mainClass: String? = null

    val mixinConfigs: List<String>? = null

    val dependencies: List<String> = ArrayList()

    val transformerClass: String? = null

    val author: String? = null
}
