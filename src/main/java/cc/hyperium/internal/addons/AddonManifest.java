package cc.hyperium.internal.addons;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({ "unused", "rawtypes" })
public final class AddonManifest {
    private String name;
    private String version;
    private String mainClass;
    private List mixinConfigs;
    @Deprecated
    private List dependencies = new ArrayList();
    private String tweakerClass;

    public String getName() {
        return this.name;
    }

    public void setName(String var1) {
        this.name = var1;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String var1) {
        this.version = var1;
    }

    public void setMainClass(String var1) {
        this.mainClass = var1;
    }

    public String getMainClass() {
        return this.mainClass;
    }

    public List getMixinConfigs() {
        return this.mixinConfigs;
    }

    public String getTweakerClass() {
        return this.tweakerClass;
    }

    @Deprecated
    public void setDependencies(List dependencies) {
        this.dependencies = dependencies;
    }

    @Deprecated
    public List getDependencies() {
        return this.dependencies;
    }

    public void setMixinConfigs(List mixinConfigs) {
        this.mixinConfigs = mixinConfigs;
    }

    public void setTweakerClass(String tweakerClass) {
        this.tweakerClass = tweakerClass;
    }
}
