package cc.hyperium.addons.bossbar.config;

import cc.hyperium.Hyperium;
import rocks.rdil.simpleconfig.Option;

public class BossbarConfig {
    public BossbarConfig() {
        Hyperium.CONFIG.register(this);
    }

    @Option public static boolean bossBarEnabled = true;
    @Option public static boolean barEnabled = true;
    @Option public static boolean textEnabled = true;
    @Option public static int x = -1;
    @Option public static int y = 12;
}
