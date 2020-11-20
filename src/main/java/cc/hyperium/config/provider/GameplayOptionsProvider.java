package cc.hyperium.config.provider;

import cc.hyperium.config.ToggleSetting;
import rocks.rdil.simpleconfig.Option;

public class GameplayOptionsProvider implements IOptionSetProvider {
    public static final GameplayOptionsProvider INSTANCE = new GameplayOptionsProvider();

    @Override
    public String getName() {
        return "Gameplay";
    }

    @Option @ToggleSetting(name = "Arrow Count While Holding Bow")
    public static boolean ARROW_COUNT = true;

    @Option @ToggleSetting(name = "Enchants Above Hotbar")
    public static boolean ENCHANTMENTS_ABOVE_HOTBAR = true;

    @Option @ToggleSetting(name = "Attack Damage Above Hotbar")
    public static boolean DAMAGE_ABOVE_HOTBAR = true;

    @Option @ToggleSetting(name = "Show Armor Protection in Inventory")
    public static boolean ARMOR_PROT_POTENTIONAL = true;

    @Option @ToggleSetting(name = "Show Armor Projectile Protection (inventory)")
    public static boolean ARMOR_PROJ_POTENTIONAL = true;

    @Option @ToggleSetting(name = "Hotbar Keys")
    public static boolean HOTBAR_KEYS = false;

    @Option @ToggleSetting(name = "Enable Crosshair in F5")
    public static boolean CROSSHAIR_IN_F5 = false;

    @Option @ToggleSetting(name = "Enable ToggleSprint")
    public static boolean ENABLE_TOGGLE_SPRINT = true;
}
