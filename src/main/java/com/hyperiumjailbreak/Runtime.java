package com.hyperiumjailbreak;

import cc.hyperium.installer.InstallerMain;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

public class Runtime {
    public static void main(String[] args) {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.hyperium.json");
        InstallerMain.main("local");
    }
}
