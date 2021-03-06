/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.launch;

import cc.hyperium.internal.addons.AddonBootstrap;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HyperiumTweaker implements ITweaker {
    public static HyperiumTweaker INSTANCE;
    private final ArrayList<String> args = new ArrayList<>();
    public static final Logger LOGGER = LogManager.getLogger();

    public HyperiumTweaker() {
        INSTANCE = this;
        if ("true".equals(System.getenv("FORCE_MIXIN_DEBUG"))) {
            System.setProperty("mixin.debug", "true");
        }
    }

    @Override
    public void acceptOptions(List<String> args, File gameDir, final File assetsDir, String profile) {
        this.args.addAll(args);
        addArg("gameDir", gameDir);
        addArg("assetsDir", assetsDir);
        addArg("version", profile);
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        // optifine can still be present without the environment specifically being optifine
        // classLoader.registerTransformer("cc.hyperium.mods.memoryfix.ClassTransformer");

        LOGGER.info("Launching addons.");
        AddonBootstrap.INSTANCE.init();

        MixinBootstrap.init();

        final MixinEnvironment environment = MixinEnvironment.getDefaultEnvironment();
        environment.setSide(MixinEnvironment.Side.CLIENT);
        LOGGER.info("Found environment: PRIMARY (obfuscation context = notch)");
        environment.setObfuscationContext("notch");

        Mixins.addConfiguration("mixins.hyperium.json");
        AddonBootstrap.INSTANCE.callAddonMixinBootstrap();
    }

    @Override
    public String[] getLaunchArguments() {
        return args.toArray(new String[]{});
    }

    private void addArg(String label, String value) {
        if (!this.args.contains(("--" + label)) && value != null) {
            this.args.add(("--" + label));
            this.args.add(value);
        }
    }

    private void addArg(String args, File file) {
        if (file == null) return;
        addArg(args, file.getAbsolutePath());
    }
}
