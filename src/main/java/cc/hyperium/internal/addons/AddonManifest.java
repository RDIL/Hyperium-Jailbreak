package cc.hyperium.internal.addons;

import java.util.List;

/**
 * Represents an addon's manifest file.
 */
@SuppressWarnings({ "unused", "rawtypes" })
public final class AddonManifest {
    private String name;
    private String version;
    private String mainClass;
    private List mixinConfigs;
    private String tweakerClass;

    /**
     * Get the name of the addon.
     *
     * @return The name.
     */
    public String getName() {
        return this.name;
    }

    public void setName(String var1) {
        this.name = var1;
    }

    /**
     * Get the version of the addon.
     *
     * @return The version.
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Set the version of the addon to the specified value.
     *
     * @param var1 The new version value.
     */
    public void setVersion(String var1) {
        this.version = var1;
    }

    /**
     * Set the main class of the addon to the specified value.
     * Warning: this does nothing if {@link AddonMinecraftBootstrap#init()} has already been called.
     *
     * @param var1 The new main class.
     */
    public void setMainClass(String var1) {
        this.mainClass = var1;
    }

    /**
     * Get the main class of the addon.
     *
     * @return The main class.
     */
    public String getMainClass() {
        return this.mainClass;
    }

    /**
     * Get the addon's mixin configurations.
     *
     * @return The mixin configurations, or null if it isn't set.
     */
    public List getMixinConfigs() {
        return this.mixinConfigs;
    }

    /**
     * This is experimental. Use at your own risk.
     *
     * @return The tweaker class, or null if it hasn't been set.
     */
    public String getTweakerClass() {
        return this.tweakerClass;
    }

    /**
     * @deprecated This no longer does anything functionally.
     */
    @Deprecated
    public final void setDependencies(List dependencies) {
    }

    /**
     * Get the addon's dependencies.
     *
     * @return Always returns null.
     * @deprecated Addon dependencies no longer work.
     */
    @Deprecated
    public final List getDependencies() {
        return null;
    }

    /**
     * Set the mixin configuration list for the addon to the specified value.
     * Warning: this does nothing if {@link AddonBootstrap#init()} has already been called.
     *
     * @param mixinConfigs The mixin configurations.
     */
    public void setMixinConfigs(List mixinConfigs) {
        this.mixinConfigs = mixinConfigs;
    }

    /**
     * This is experimental. Use at your own risk.
     *
     * @param tweakerClass The tweaker class.
     */
    public void setTweakerClass(String tweakerClass) {
        this.tweakerClass = tweakerClass;
    }
}
