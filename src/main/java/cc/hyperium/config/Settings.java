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
import cc.hyperium.styles.ButtonType;
import cc.hyperium.styles.GuiStyle;
import static cc.hyperium.config.Category.*;

public class Settings {
    public static final Settings INSTANCE = new Settings();

    @ConfigOpt @ToggleSetting(name = "gui.settings.FPS", category = INTEGRATIONS)
    public static boolean FPS = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.fullbright", category = INTEGRATIONS)
    public static boolean FULLBRIGHT = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.romannumerals")
    public static boolean ROMAN_NUMERALS = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.compactchat", category = INTEGRATIONS)
    public static boolean COMPACT_CHAT = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.voidflickerfix", category = IMPROVEMENTS)
    public static boolean VOID_FLICKER_FIX = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.fastchat", category = INTEGRATIONS)
    public static boolean FASTCHAT = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.shinypotions", category = ANIMATIONS)
    public static boolean SHINY_POTS = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.smartsounds", category = IMPROVEMENTS)
    public static boolean SMART_SOUNDS = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.numericping", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean NUMBER_PING = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.arrowcountwhenholdingbow", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean ARROW_COUNT = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.showenchantmentsabovehotbar", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean ENCHANTMENTS_ABOVE_HOTBAR = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.showattackdamageabovehotbar", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean DAMAGE_ABOVE_HOTBAR = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.armorprotectionpotentional", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean ARMOR_PROT_POTENTIONAL = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.armorprojectileprotpotentional", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean ARMOR_PROJ_POTENTIONAL = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.hotbarkeys", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean HOTBAR_KEYS = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.hidecrosshairinf5", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean CROSSHAIR_IN_F5 = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.critparticlefix", category = IMPROVEMENTS)
    public static boolean CRIT_FIX = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.holdperspectivekey")
    public static boolean PERSPECTIVE_HOLD = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.windowedfullscreen", category = IMPROVEMENTS)
    public static boolean WINDOWED_FULLSCREEN = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.staticfov", category = IMPROVEMENTS)
    public static boolean STATIC_FOV = false;

    @ConfigOpt @SelectorSetting(name = "Hat Type", category = COSMETICS, items = "NONE")
    public static String HAT_TYPE = "NONE";

    @ConfigOpt @SelectorSetting(name = "Companion Type", category = COSMETICS, items = "NONE")
    public static String COMPANION_TYPE = "NONE";

    @ConfigOpt @ToggleSetting(category = GENERAL, name = "Show own name tag")
    public static boolean SHOW_OWN_NAME = false;

    @ConfigOpt @ToggleSetting(category = COSMETICS, name = "gui.settings.showcosmeticseverywhere")
    public static boolean SHOW_COSMETICS_EVERYWHERE = true;

    @ConfigOpt public static boolean DAB_TOGGLE = false;

    @ConfigOpt public static boolean TPOSE_TOGGLE = false;

    @ConfigOpt public static boolean FLOSS_TOGGLE = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.17blockhitting", category = ANIMATIONS)
    public static boolean OLD_BLOCKHIT = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.17bowposition", category = ANIMATIONS)
    public static boolean OLD_BOW = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.17rodposition", category = ANIMATIONS)
    public static boolean OLD_ROD = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.17redarmour", category = ANIMATIONS)
    public static boolean OLD_ARMOUR = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.17eating", category = ANIMATIONS)
    public static boolean OLD_EATING = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.17sneakinganimation", category = ANIMATIONS)
    public static boolean OLD_SNEAKING = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.17blocking", category = ANIMATIONS)
    public static boolean OLD_BLOCKING = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.17itemheld", category = ANIMATIONS)
    public static boolean OLD_ITEM_HELD = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.17debug", category = ANIMATIONS)
    public static boolean OLD_DEBUG = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.17health", category = ANIMATIONS)
    public static boolean OLD_HEALTH = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.customswordanimation", category = ANIMATIONS)
    public static boolean CUSTOM_SWORD_ANIMATION = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.fliptogglemode", category = COSMETICS)
    public static boolean isFlipToggle = true;

    @ConfigOpt public static int flipType = 1;

    @ConfigOpt @SelectorSetting(name = "gui.settings.fliptype", category = COSMETICS, items = {}) // OVERRIDEN
    public static String FLIP_TYPE_STRING = "FLIP";

    @ConfigOpt @ToggleSetting(name = "gui.settings.tposetogglemode", category = COSMETICS)
    public static boolean TPOSE_TOGGLE_MODE = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.transparentcontainerbackgrounds")
    public static boolean FAST_CONTAINER = false;

    @ConfigOpt @SelectorSetting(name = "gui.settings.particlesmode", items = {"OFF", "PLAIN 1", "PLAIN 2", "CHROMA 1", "CHROMA 2"}, category = COSMETICS)
    public static String PARTICLE_MODE = "OFF";

    @ConfigOpt public static int MAX_PARTICLES = 200;

    @ConfigOpt @SelectorSetting(name = "gui.settings.maxparticles", category = COSMETICS, items = {"200"})
    public static String MAX_PARTICLE_STRING = "200";

    @ConfigOpt public static double HEAD_SCALE_FACTOR = 1.0;

    @ConfigOpt @SelectorSetting(name = "gui.settings.headitemscale", category = ANIMATIONS, items = {"1.0", "1.25", "1.5", "1.75", "2.0", "2.5"})
    public static String HEAD_SCALE_FACTOR_STRING = "1.0";

    @ConfigOpt @ToggleSetting(name = "gui.settings.particlesininventory", category = COSMETICS)
    public static boolean PARTICLES_INV = true;

    @ConfigOpt @SelectorSetting(name = "gui.settings.deadmau5ears", category = COSMETICS, items = {})
    public static String EARS_STATE = "ON";

    @ConfigOpt @ToggleSetting(category = MISC, name = "gui.settings.showconfirmationpopup")
    public static boolean SHOW_INGAME_CONFIRMATION_POPUP = true;

    @ConfigOpt @ToggleSetting(category = HYPIXEL, name = "gui.settings.friendsfirstintab")
    public static boolean FRIENDS_FIRST_IN_TAB = true;

    @ConfigOpt @ToggleSetting(category = GENERAL, name = "gui.settings.onlineindicator")
    public static boolean SHOW_ONLINE_PLAYERS = true;

    @ConfigOpt @ToggleSetting(category = HYPIXEL, name = "gui.settings.pingondm")
    public static boolean PING_ON_DM = true;

    @ConfigOpt @ToggleSetting(category = GENERAL, name = "gui.settings.sprintbypassstaticfov")
    public static boolean staticFovSprintModifier;

    @ConfigOpt @ToggleSetting(category = GENERAL, name = "gui.settings.sprintandperspectivemessages")
    public static boolean SPRINT_PERSPECTIVE_MESSAGES = true;

    @ConfigOpt @SelectorSetting(name = "gui.settings.showdragonhead", items = {}, category = COSMETICS)
    public static String SHOW_DRAGON_HEAD = "OFF";

    @ConfigOpt @SelectorSetting(name = "gui.settings.showwings", items = {}, category = COSMETICS)
    public static String SHOW_WINGS = "ON";

    @ConfigOpt @SliderSetting(name = "gui.settings.wingsheight", min = -40, max = 40, category = COSMETICS)
    public static double WINGS_OFFSET = 0D;

    @ConfigOpt @SliderSetting(name = "gui.settings.wingsscale", min = 50, max = 200, category = COSMETICS)
    public static double WINGS_SCALE = 100D;

    @ConfigOpt @ToggleSetting(name = "gui.settings.disable_dances", category = COSMETICS)
    public static boolean DISABLE_DANCES = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.showchromahud", category = CHROMAHUD, mods = true)
    public static boolean SHOW_CHROMAHUD = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.chromahudprefixsquarebrace", category = CHROMAHUD, mods = true)
    public static boolean CHROMAHUD_SQUAREBRACE_PREFIX_OPTION = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.directionhudshort", category = CHROMAHUD, mods = true)
    public static boolean SHORT_DIRECTION_HUD = false;

    @ConfigOpt @SelectorSetting(name = "gui.settings.mainmenustyle", items = {"HYPERIUM", "DEFAULT"}, category = MENUS)
    public static String MENU_STYLE = GuiStyle.DEFAULT.name();

    @ConfigOpt @SelectorSetting(name = "gui.settings.pausemenustyle", items = {"HYPERIUM", "DEFAULT"}, category = MENUS)
    public static String PAUSE_STYLE = GuiStyle.HYPERIUM.name();

    @ConfigOpt @ToggleSetting(name = "gui.settings.fastworldswitching", category = IMPROVEMENTS)
    public static boolean FAST_WORLD_LOADING = false;

    @ConfigOpt public static int MAX_WORLD_PARTICLES_INT = 10000;

    @ConfigOpt @SelectorSetting(category = IMPROVEMENTS, name = "gui.settings.maxworldparticles", items = {"1000", "2000", "4000", "6000", "8000", "10000", "20000", "50000",})
    public static String MAX_WORLD_PARTICLES_STRING = "10000";

    @ConfigOpt @ToggleSetting(name = "Show Companion in 1st person", category = COSMETICS)
    public static boolean SHOW_COMPANION_IN_1ST_PERSON = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.showhitdistances", category = REACH, mods = true)
    public static boolean SHOW_HIT_DISTANCES = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.autofriendenabled", category = AUTOFRIEND, mods = true)
    public static boolean AUTOFRIEND_TOGGLE = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.showfriendmessages", category = AUTOFRIEND, mods = true)
    public static boolean AUTOFRIEND_MESSAGES = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.fortnitecompassenabled", category = FNCOMPASS, mods = true)
    public static boolean FNCOMPASS_ENABLED = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.showbackground", category = FNCOMPASS, mods = true)
    public static boolean FNCOMPASS_BACKGROUND = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.chroma", category = FNCOMPASS, mods = true)
    public static boolean FNCOMPASS_CHROMA = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.shadow", category = FNCOMPASS, mods = true)
    public static boolean FNCOMPASS_SHADOW = true;

    @ConfigOpt
    @SelectorSetting(category = FNCOMPASS, name = "gui.settings.details", items = {"0", "1", "2"}, mods = true)
    public static String FNCOMPASS_DETAILS = "2";

    @ToggleSetting(name = "gui.settings.showuserdotsonnametags", category = INTEGRATIONS)
    public static boolean SHOW_DOTS_ON_NAME_TAGS = false;

    @ConfigOpt @SelectorSetting(name = "gui.settings.colortype", mods = true, category = REACH, items = {"RGB", "CHROMA"})
    public static String REACH_COLOR_TYPE = "RGB";

    @ConfigOpt @SliderSetting(name = "gui.settings.red", mods = true, category = REACH, isInt = true, min = 0, max = 255)
    public static int REACH_RED = 255;

    @ConfigOpt @SliderSetting(name = "gui.settings.blue", mods = true, category = REACH, isInt = true, min = 0, max = 255)
    public static int REACH_BLUE = 0;

    @ConfigOpt @SliderSetting(name = "gui.settings.green", mods = true, category = REACH, isInt = true, min = 0, max = 255)
    public static int REACH_GREEN = 255;

    @ConfigOpt @ToggleSetting(name = "gui.settings.togglesprint", category = TOGGLESPRINT, mods = true)
    public static boolean ENABLE_TOGGLE_SPRINT = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.broadcastachievements", category = HYPIXEL)
    public static boolean BROADCAST_ACHIEVEMENTS = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.hyperiumprefix", category = GENERAL)
    public static boolean HYPERIUM_CHAT_PREFIX = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.sendguildwelcomemessage", category = HYPIXEL)
    public static boolean SEND_GUILD_WELCOME_MESSAGE = true;

    @ConfigOpt @ToggleSetting(name = "gui.settings.thankwatchdog", category = HYPIXEL)
    public static boolean THANK_WATCHDOG = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.itemphysics", category = ITEM_PHYSIC, mods = true)
    public static boolean ITEM_PHYSIC_ENABLED = false;

    @ConfigOpt public static String GUI_FONT = "Roboto Condensed";

    @ConfigOpt @ToggleSetting(name = "gui.settings.betterf1", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean BETTERF1 = false;

    @ConfigOpt @SelectorSetting(name = "gui.settings.buttonstyle", category = BUTTONS, items = {"HYPERIUM", "HYPERIUM 2"})
    public static String BUTTON_STYLE = ButtonStyle.HYPERIUM.name();

    @ConfigOpt @SliderSetting(name = "gui.settings.buttonred", isInt = true, min = 0, max = 255, category = BUTTONS)
    public static int BUTTON_RED = 255;

    @ConfigOpt @SliderSetting(name = "gui.settings.buttongreen", isInt = true, min = 0, max = 255, category = BUTTONS)
    public static int BUTTON_GREEN = 255;

    @ConfigOpt @SliderSetting(name = "gui.settings.buttonblue", isInt = true, min = 0, max = 255, category = BUTTONS)
    public static int BUTTON_BLUE = 255;

    @ConfigOpt @SelectorSetting(name = "gui.settings.buttontype", category = BUTTONS, items = {"DEFAULT", "RGB", "CHROMA"})
    public static String BUTTON_TYPE = ButtonType.DEFAULT.name();

    @ConfigOpt @ToggleSetting(name = "gui.settings.disableenchantglint", category = IMPROVEMENTS)
    public static boolean DISABLE_ENCHANT_GLINT = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.disabletitles", category = VANILLA_ENHANCEMENTS, mods = true)
    public static boolean HIDE_TITLES = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.disablelightning", category = IMPROVEMENTS)
    public static boolean DISABLE_LIGHTNING = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.disablearmorstands", category = IMPROVEMENTS)
    public static boolean DISABLE_ARMORSTANDS = false;

    @ConfigOpt @ToggleSetting(name = "gui.settings.disableitemframes", category = IMPROVEMENTS)
    public static boolean DISABLE_ITEMFRAMES = false;

    @ConfigOpt @ToggleSetting(name ="gui.settings.shinypotions.matchcolor", category = ANIMATIONS)
    public static boolean SHINY_POTS_MATCH_COLOR = false;

    @ConfigOpt @ToggleSetting(name = "Disable Snooper", category = GENERAL)
    public static boolean NOSNOOPER = false;

    @ConfigOpt @ToggleSetting(name = "Hide Skeleton Hat Bones", category = HYPIXELSKYBLOCK)
    public static boolean hide_skeletonhat_bones = false;

    public static void register() {
        Hyperium.CONFIG.register(INSTANCE);
    }
    public static void save() {
        Hyperium.CONFIG.save();
    }
}
