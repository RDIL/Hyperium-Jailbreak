package cc.hyperium.internal.addons;

import java.util.ArrayList;
import java.util.List;

/**
 * An addon's manifest.
 */
@SuppressWarnings({ "unused", "rawtypes" })
public class AddonManifest {
    private String name;
    private String version;
    private String mainClass;
    private List mixinConfigs;
    private List dependencies = new ArrayList();
    private String transformerClass;

    /**
     * Get the addon's name.
     *
     * @return The addon's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the addon's name.
     *
     * @param var1 The new name for the addon.
     */
    public void setName(String var1) {
        this.name = var1;
    }

    /**
     * Get the addon's version.
     *
     * @return The addon's version.
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Set the addon's version.
     *
     * @param var1 The new version
     */
    public void setVersion(String var1) {
        this.version = var1;
    }

    /**
     * Set the addon's main class.
     * The main class must implement {@link cc.hyperium.internal.addons.IAddon}.
     *
     * @param var1 The new main class.
     */
    public void setMainClass(String var1) {
        this.mainClass = var1;
    }

    /**
     * Set the addon's {@link java.util.List} of Mixin configurations.
     *
     * @param var1 The {@link java.util.List} of Mixin configuration names.
     */
    public void setMixinConfigs(List var1) {
        this.mixinConfigs = var1;
    }

    /**
     * Set the addon's transformer class.
     * The transformer class must implement {@link net.minecraft.launchwrapper.IClassTransformer}.
     *
     * @param var1 The new transformer class.
     */
    public void setTransformerClass(String var1) {
        this.transformerClass = var1;
    }

    /**
     * Get the addon's main class.
     *
     * @return The addon's main class.
     */
    public String getMainClass() {
        return this.mainClass;
    }

    /**
     * Get the addon's {@link java.util.List} of Mixin configuration names.
     *
     * @return The addon's {@link java.util.List} of Mixin configuration names.
     */
    public List getMixinConfigs() {
        return this.mixinConfigs;
    }

    /**
     * Get the addon's dependencies.
     *
     * @return The addon's dependencies.
     * @deprecated HyperiumJailbreak no longer supports addon dependencies due to class loading limitations.
     */
    @Deprecated
    public List getDependencies() {
        return this.dependencies;
    }

    /**
     * Get the addon's transformer class.
     *
     * @return The addon's transformer class.
     */
    public String getTransformerClass() {
        return this.transformerClass;
    }
}
