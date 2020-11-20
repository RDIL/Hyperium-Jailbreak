package cc.hyperium.config.provider;

import cc.hyperium.config.SelectorSetting;
import cc.hyperium.config.ToggleSetting;
import rocks.rdil.simpleconfig.Option;

public class AnimationOptionsProvider implements IOptionSetProvider {
    public static final AnimationOptionsProvider INSTANCE = new AnimationOptionsProvider();

    @Override
    public String getName() {
        return "Animations";
    }
    
    @Option @SelectorSetting(name = "Head Item Scale", items = {"1.0", "1.25", "1.5", "1.75", "2.0", "2.5"})
    public static String HEAD_SCALE_FACTOR_STRING = "1.0";

    @Option @ToggleSetting(name = "Old Block Hitting")
    public static boolean OLD_BLOCKHIT = true;

    @Option @ToggleSetting(name = "Old Bow Position")
    public static boolean OLD_BOW = true;

    @Option @ToggleSetting(name = "Old Rod Position")
    public static boolean OLD_ROD = true;

    @Option @ToggleSetting(name = "Old Armor Flashing")
    public static boolean OLD_ARMOUR = true;

    @Option @ToggleSetting(name = "Old Eating")
    public static boolean OLD_EATING = true;

    @Option @ToggleSetting(name = "Old Sneaking")
    public static boolean OLD_SNEAKING = false;

    @Option @ToggleSetting(name = "Old Block Placing")
    public static boolean OLD_BLOCKING = false;

    @Option @ToggleSetting(name = "Old Item Positioning")
    public static boolean OLD_ITEM_HELD = false;

    @Option @ToggleSetting(name = "Old Debug Menu")
    public static boolean OLD_DEBUG = false;

    @Option @ToggleSetting(name = "Old Health")
    public static boolean OLD_HEALTH = false;

    @Option @ToggleSetting(name = "Shiny Potions")
    public static boolean SHINY_POTS = false;

    @Option @ToggleSetting(name = "Shiny Potions: Match Color")
    public static boolean SHINY_POTS_MATCH_COLOR = false;
}
