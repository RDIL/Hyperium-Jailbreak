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

package cc.hyperium.config;

import cc.hyperium.Hyperium;
import cc.hyperium.styles.ButtonStyle;
import cc.hyperium.styles.GuiStyle;
import static cc.hyperium.config.Category.*;

public class Settings {
    public static final Settings INSTANCE = new Settings();

    @ConfigOpt @ToggleSetting(name = "FPS Mode (activates after restart)", category = INTEGRATIONS)
    public static boolean FPS = false;

    @ConfigOpt @ToggleSetting(name = "Fullbright", category = INTEGRATIONS)
    public static boolean FULLBRIGHT = true;

    @ConfigOpt @ToggleSetting(name = "Roman Numerals")
    public static boolean ROMAN_NUMERALS = true;

    @ConfigOpt @ToggleSetting(name = "Compact Chat", category = INTEGRATIONS)
    public static boolean COMPACT_CHAT = false;

    @ConfigOpt @ToggleSetting(name = "Void Flicker Fix", category = IMPROVEMENTS)
    public static boolean VOID_FLICKER_FIX = true;

    @ConfigOpt @ToggleSetting(name = "Disable Chat Background", category = INTEGRATIONS)
    public static boolean FASTCHAT = true;

    @ConfigOpt @ToggleSetting(name = "Shiny Pots", category = ANIMATIONS)
    public static boolean SHINY_POTS = false;

    @ConfigOpt @ToggleSetting(name = "Disable Sounds When Tabbed Out", category = IMPROVEMENTS)
    public static boolean SMART_SOUNDS = false;

    @ConfigOpt @ToggleSetting(name = "Arrow Count While Holding Bow", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean ARROW_COUNT = true;

    @ConfigOpt @ToggleSetting(name = "Enchants Above Hotbar", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean ENCHANTMENTS_ABOVE_HOTBAR = true;

    @ConfigOpt @ToggleSetting(name = "Attack Damage Above Hotbar", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean DAMAGE_ABOVE_HOTBAR = true;

    @ConfigOpt @ToggleSetting(name = "Show Armor Protection in Inventory", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean ARMOR_PROT_POTENTIONAL = true;

    @ConfigOpt @ToggleSetting(name = "Show Armor Projectile Protection (inventory)", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean ARMOR_PROJ_POTENTIONAL = true;

    @ConfigOpt @ToggleSetting(name = "Hotbar Keys", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean HOTBAR_KEYS = false;

    @ConfigOpt @ToggleSetting(name = "Enable Crosshair in F5", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean CROSSHAIR_IN_F5 = false;

    @ConfigOpt @ToggleSetting(name = "Critical Particle Fix", category = IMPROVEMENTS)
    public static boolean CRIT_FIX = true;

    @ConfigOpt @ToggleSetting(name = "Require Holding Perspective Key")
    public static boolean PERSPECTIVE_HOLD = false;

    @ConfigOpt @ToggleSetting(name = "Windowed Fullscreen", category = IMPROVEMENTS)
    public static boolean WINDOWED_FULLSCREEN = false;

    @ConfigOpt @ToggleSetting(name = "Static FOV", category = IMPROVEMENTS)
    public static boolean STATIC_FOV = false;

    @ConfigOpt @SelectorSetting(name = "Hat Type", category = COSMETICS, items = "NONE")
    public static String HAT_TYPE = "NONE";

    @ConfigOpt @ToggleSetting(category = GENERAL, name = "Show Own Name Tag")
    public static boolean SHOW_OWN_NAME = false;

    @ConfigOpt @ToggleSetting(category = COSMETICS, name = "Show Cosmetics Everywhere")
    public static boolean SHOW_COSMETICS_EVERYWHERE = true;

    @ConfigOpt public static boolean DAB_TOGGLE = false;

    @ConfigOpt @ToggleSetting(name = "Old Block Hitting", category = ANIMATIONS)
    public static boolean OLD_BLOCKHIT = true;

    @ConfigOpt @ToggleSetting(name = "Old Bow Position", category = ANIMATIONS)
    public static boolean OLD_BOW = true;

    @ConfigOpt @ToggleSetting(name = "Old Rod Position", category = ANIMATIONS)
    public static boolean OLD_ROD = true;

    @ConfigOpt @ToggleSetting(name = "Old Armor Flashing", category = ANIMATIONS)
    public static boolean OLD_ARMOUR = true;

    @ConfigOpt @ToggleSetting(name = "Old Eating", category = ANIMATIONS)
    public static boolean OLD_EATING = true;

    @ConfigOpt @ToggleSetting(name = "Old Sneaking", category = ANIMATIONS)
    public static boolean OLD_SNEAKING = false;

    @ConfigOpt @ToggleSetting(name = "Old Blocking", category = ANIMATIONS)
    public static boolean OLD_BLOCKING = false;

    @ConfigOpt @ToggleSetting(name = "Old Item Positioning", category = ANIMATIONS)
    public static boolean OLD_ITEM_HELD = false;

    @ConfigOpt @ToggleSetting(name = "Old Debug Menu", category = ANIMATIONS)
    public static boolean OLD_DEBUG = false;

    @ConfigOpt @ToggleSetting(name = "Old Health", category = ANIMATIONS)
    public static boolean OLD_HEALTH = false;

    @ConfigOpt @ToggleSetting(name = "Custom Sword Animation", category = ANIMATIONS)
    public static boolean CUSTOM_SWORD_ANIMATION = false;

    @ConfigOpt @ToggleSetting(name = "Flip Toggle Mode", category = COSMETICS)
    public static boolean isFlipToggle = true;

    @ConfigOpt public static int flipType = 1;

    @ConfigOpt @SelectorSetting(name = "Flip Type", category = COSMETICS, items = {}) // OVERRIDEN
    public static String FLIP_TYPE_STRING = "FLIP";

    @ConfigOpt @ToggleSetting(name = "Transparent GUI Backgrounds")
    public static boolean FAST_CONTAINER = false;

    @ConfigOpt @SelectorSetting(name = "Particle Cosmetic Mode", items = {"OFF", "PLAIN 1", "PLAIN 2", "CHROMA 1", "CHROMA 2"}, category = COSMETICS)
    public static String PARTICLE_MODE = "OFF";

    @ConfigOpt public static int MAX_PARTICLES = 200;

    @ConfigOpt @SelectorSetting(name = "Maximum Particles", category = COSMETICS, items = {"200"})
    public static String MAX_PARTICLE_STRING = "200";

    @ConfigOpt public static double HEAD_SCALE_FACTOR = 1.0;

    @ConfigOpt @SelectorSetting(name = "Head Item Scale", category = ANIMATIONS, items = {"1.0", "1.25", "1.5", "1.75", "2.0", "2.5"})
    public static String HEAD_SCALE_FACTOR_STRING = "1.0";

    @ConfigOpt @ToggleSetting(name = "Particle Cosmetic in Inventory", category = COSMETICS)
    public static boolean PARTICLES_INV = true;

    @ConfigOpt @ToggleSetting(category = MISC, name = "Party/Friend Request Popup")
    public static boolean SHOW_INGAME_CONFIRMATION_POPUP = true;

    @ConfigOpt @ToggleSetting(category = HYPIXEL, name = "Friends First on Tab")
    public static boolean FRIENDS_FIRST_IN_TAB = true;

    @ConfigOpt @ToggleSetting(category = GENERAL, name = "Online Indicators")
    public static boolean SHOW_ONLINE_PLAYERS = true;

    @ConfigOpt @ToggleSetting(category = HYPIXEL, name = "Ping on DMs")
    public static boolean PING_ON_DM = true;

    @ConfigOpt @ToggleSetting(category = GENERAL, name = "Sprint Bypass Static FOV")
    public static boolean staticFovSprintModifier;

    @ConfigOpt @ToggleSetting(category = GENERAL, name = "Show Sprint/Perspective Messages")
    public static boolean SPRINT_PERSPECTIVE_MESSAGES = true;

    @ConfigOpt @SelectorSetting(name = "Show Dragon Head", items = {}, category = COSMETICS)
    public static String SHOW_DRAGON_HEAD = "OFF";

    @ConfigOpt @SelectorSetting(name = "Show Wings", items = {}, category = COSMETICS)
    public static String SHOW_WINGS = "ON";

    @ConfigOpt @SliderSetting(name = "Wings Height", min = -40, max = 40, category = COSMETICS)
    public static double WINGS_OFFSET = 0D;

    @ConfigOpt @SliderSetting(name = "Wings Scale", min = 50, max = 200, category = COSMETICS)
    public static double WINGS_SCALE = 100D;

    @ConfigOpt @ToggleSetting(name = "Disable Dances", category = COSMETICS)
    public static boolean DISABLE_DANCES = false;

    @ConfigOpt @ToggleSetting(name = "Show ChromaHUD", category = CHROMAHUD, mods = true)
    public static boolean SHOW_CHROMAHUD = true;

    @ConfigOpt @ToggleSetting(name = "Enable Square Brace Prefix", category = CHROMAHUD, mods = true)
    public static boolean CHROMAHUD_SQUAREBRACE_PREFIX_OPTION = false;

    @ConfigOpt @ToggleSetting(name = "Sort Direction HUD Messages", category = CHROMAHUD, mods = true)
    public static boolean SHORT_DIRECTION_HUD = false;

    @ConfigOpt @SelectorSetting(name = "Main Menu Style", items = {"HYPERIUM", "DEFAULT"}, category = MENUS)
    public static String MENU_STYLE = GuiStyle.DEFAULT.name();

    @ConfigOpt @SelectorSetting(name = "Pause Menu Style", items = {"HYPERIUM", "DEFAULT"}, category = MENUS)
    public static String PAUSE_STYLE = GuiStyle.HYPERIUM.name();

    @ConfigOpt @ToggleSetting(name = "Fast World Loading (! Causes Performance Issues !)", category = IMPROVEMENTS)
    public static boolean FAST_WORLD_LOADING = false;

    @ConfigOpt public static int MAX_WORLD_PARTICLES_INT = 10000;

    @ConfigOpt @SelectorSetting(category = IMPROVEMENTS, name = "Max World Particles", items = {"1000", "2000", "4000", "6000", "8000", "10000", "20000", "50000",})
    public static String MAX_WORLD_PARTICLES_STRING = "10000";

    @ConfigOpt @ToggleSetting(name = "Show Hit Distances", category = REACH, mods = true)
    public static boolean SHOW_HIT_DISTANCES = false;

    @ConfigOpt @ToggleSetting(name = "Enable AutoFriend", category = AUTOFRIEND, mods = true)
    public static boolean AUTOFRIEND_TOGGLE = false;

    @ConfigOpt @ToggleSetting(name = "Show Friend Messages", category = AUTOFRIEND, mods = true)
    public static boolean AUTOFRIEND_MESSAGES = true;

    @ConfigOpt @ToggleSetting(name = "Enable Fortnite Compass", category = FNCOMPASS, mods = true)
    public static boolean FNCOMPASS_ENABLED = false;

    @ConfigOpt @ToggleSetting(name = "Show Background", category = FNCOMPASS, mods = true)
    public static boolean FNCOMPASS_BACKGROUND = true;

    @ConfigOpt @ToggleSetting(name = "Enable Chroma", category = FNCOMPASS, mods = true)
    public static boolean FNCOMPASS_CHROMA = false;

    @ConfigOpt @ToggleSetting(name = "Enable Text Shadow", category = FNCOMPASS, mods = true)
    public static boolean FNCOMPASS_SHADOW = true;

    @ConfigOpt
    @SelectorSetting(category = FNCOMPASS, name = "Detail Level", items = {"0", "1", "2"}, mods = true)
    public static String FNCOMPASS_DETAILS = "2";

    @ToggleSetting(name = "Show Dots on Name Tags", category = INTEGRATIONS)
    public static boolean SHOW_DOTS_ON_NAME_TAGS = false;

    @ConfigOpt @SelectorSetting(name = "Reach Color Type", mods = true, category = REACH, items = {"RGB", "CHROMA"})
    public static String REACH_COLOR_TYPE = "RGB";

    @ConfigOpt @SliderSetting(name = "Red", mods = true, category = REACH, isInt = true, min = 0, max = 255)
    public static int REACH_RED = 255;

    @ConfigOpt @SliderSetting(name = "Blue", mods = true, category = REACH, isInt = true, min = 0, max = 255)
    public static int REACH_BLUE = 0;

    @ConfigOpt @SliderSetting(name = "Green", mods = true, category = REACH, isInt = true, min = 0, max = 255)
    public static int REACH_GREEN = 255;

    @ConfigOpt @ToggleSetting(name = "Enable ToggleSprint", category = TOGGLESPRINT, mods = true)
    public static boolean ENABLE_TOGGLE_SPRINT = true;

    @ConfigOpt @ToggleSetting(name = "Broadcast Achivements", category = HYPIXEL)
    public static boolean BROADCAST_ACHIEVEMENTS = true;

    @ConfigOpt @ToggleSetting(name = "Hyperium Chat Prefix", category = GENERAL)
    public static boolean HYPERIUM_CHAT_PREFIX = true;

    @ConfigOpt @ToggleSetting(name = "Guild Welcome Message", category = HYPIXEL)
    public static boolean SEND_GUILD_WELCOME_MESSAGE = true;

    @ConfigOpt @ToggleSetting(name = "Thank Watchdog", category = HYPIXEL)
    public static boolean THANK_WATCHDOG = false;

    @ConfigOpt @ToggleSetting(name = "Enable Item Physics", category = ITEM_PHYSIC, mods = true)
    public static boolean ITEM_PHYSIC_ENABLED = false;

    @ConfigOpt public static String GUI_FONT = "Roboto Condensed";

    @ConfigOpt @ToggleSetting(name = "Hide Name Tags in F1", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean BETTERF1 = false;

    @ConfigOpt @SelectorSetting(name = "Button Style", category = BUTTONS, items = {"HYPERIUM", "HYPERIUM 2"})
    public static String BUTTON_STYLE = ButtonStyle.HYPERIUM.name();

    @ConfigOpt @ToggleSetting(name = "Chroma Buttons (For Hyperium 2 Style)", category = BUTTONS)
    public static boolean H2_BUTTONS_CHROMA = false;

    @ConfigOpt @ToggleSetting(name = "Disable Enchant Glint", category = IMPROVEMENTS)
    public static boolean DISABLE_ENCHANT_GLINT = false;

    @ConfigOpt @ToggleSetting(name = "Disable Titles", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean HIDE_TITLES = false;

    @ConfigOpt @ToggleSetting(name = "Disable Lightning", category = IMPROVEMENTS)
    public static boolean DISABLE_LIGHTNING = false;

    @ConfigOpt @ToggleSetting(name = "Disable Armor Stands", category = IMPROVEMENTS)
    public static boolean DISABLE_ARMORSTANDS = false;

    @ConfigOpt @ToggleSetting(name = "Disable Item Frames", category = IMPROVEMENTS)
    public static boolean DISABLE_ITEMFRAMES = false;

    @ConfigOpt @ToggleSetting(name = "Shiny Potions: Match Color", category = ANIMATIONS)
    public static boolean SHINY_POTS_MATCH_COLOR = false;

    @ConfigOpt @ToggleSetting(name = "Hide Skeleton Hat Bones", category = HYPIXELSKYBLOCK)
    public static boolean hide_skeletonhat_bones = false;

    @ConfigOpt @ToggleSetting(name = "BETA - Faster Particle Rendering", category = IMPROVEMENTS)
    public static boolean IMPROVE_PARTICLE_PERF = false;

    @ConfigOpt @ToggleSetting(name = "Blink at the End of Night Vision", category = GENERAL)
    public static boolean NIGHT_VISION_BLINKING = false;

    public static void register() {
        Hyperium.CONFIG.register(INSTANCE);
    }
    public static void save() {
        Hyperium.CONFIG.save();
    }
}
