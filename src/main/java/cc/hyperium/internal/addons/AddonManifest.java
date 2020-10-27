package cc.hyperium.internal.addons;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({ "unused", "rawtypes" })
public final class AddonManifest {
    private String name;
    private String version;
    private String mainClass;
    private List mixinConfigs;
    private List dependencies = new ArrayList();
    private String transformerClass;

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

    public void setMixinConfigs(List var1) {
        this.mixinConfigs = var1;
    }

    public void setTransformerClass(String var1) {
        this.transformerClass = var1;
    }

    public String getMainClass() {
        return this.mainClass;
    }

    public List getMixinConfigs() {
        return this.mixinConfigs;
    }

    public List getDependencies() {
        return this.dependencies;
    }

    public String getTransformerClass() {
        return this.transformerClass;
    }
}
