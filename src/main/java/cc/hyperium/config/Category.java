package cc.hyperium.config;

public enum Category {
    GENERAL("General"),
    IMPROVEMENTS("Improvements"),
    INTEGRATIONS("Integrations"),
    COSMETICS("Cosmetics"),
    ANIMATIONS("Animations"),
    MISC("Misc"),
    MODS("Mods"),
    HYPIXEL("Hypixel"),
    BUTTONS("Buttons"),
    MENUS("Menus"),
    AUTOTIP("Autotip"),
    AUTO_GG("Auto GG"),
    LEVEL_HEAD("Levelhead"),
    REACH("Reach Display"),
    VANILLA_ENHANCEMENTS("Vanilla Enhancements"),
    CHROMAHUD("ChromaHUD"),
    KEYSTROKES("Keystrokes"),
    AUTOFRIEND("Auto Friend"),
    GLINTCOLORIZER("Glint Colorizer"),
    FNCOMPASS("Fortnite Compass"),
    TAB_TOGGLE("Tab Toggle"),
    ITEM_PHYSIC("Item Physics"),
    TOGGLESPRINT("Toggle Sprint"),
    HYPIXELSKYBLOCK("SkyBlock");

    private String display;

    Category(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
