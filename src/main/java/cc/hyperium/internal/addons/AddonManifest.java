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

    public final String getName() {
        return this.name;
    }

    public final void setName(String var1) {
        this.name = var1;
    }

    public final String getVersion() {
        return this.version;
    }

    public final void setVersion(String var1) {
        this.version = var1;
    }

    public final void setMainClass(String var1) {
        this.mainClass = var1;
    }

    public final void setMixinConfigs(List var1) {
        this.mixinConfigs = var1;
    }

    public final void setTransformerClass(String var1) {
        this.transformerClass = var1;
    }

    public final String getMainClass() {
        return this.mainClass;
    }

    public final List getMixinConfigs() {
        return this.mixinConfigs;
    }

    public final List getDependencies() {
        return this.dependencies;
    }

    public final String getTransformerClass() {
        return this.transformerClass;
    }
}
