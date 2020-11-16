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
import rocks.rdil.simpleconfig.Option;

public class Settings {
    public static final Settings INSTANCE = new Settings();

    @Option @ToggleSetting(name = "Show ChromaHUD", category = CHROMAHUD)
    public static boolean SHOW_CHROMAHUD = true;

    @Option @ToggleSetting(name = "Enable Square Brace Prefix", category = CHROMAHUD)
    public static boolean CHROMAHUD_SQUAREBRACE_PREFIX_OPTION = false;

    @Option @ToggleSetting(name = "Short Direction HUD", category = CHROMAHUD)
    public static boolean SHORT_DIRECTION_HUD = false;

    @Option @SelectorSetting(name = "Main Menu Style", items = {"HYPERIUM", "DEFAULT"}, category = MENUS)
    public static String MENU_STYLE = GuiStyle.DEFAULT.name();

    @Option @SelectorSetting(name = "Pause Menu Style", items = {"HYPERIUM", "DEFAULT"}, category = MENUS)
    public static String PAUSE_STYLE = GuiStyle.HYPERIUM.name();

    @Option @ToggleSetting(name = "Enable AutoFriend", category = AUTOFRIEND)
    public static boolean AUTOFRIEND_TOGGLE = false;

    @Option @ToggleSetting(name = "Show Friend Messages", category = AUTOFRIEND)
    public static boolean AUTOFRIEND_MESSAGES = true;

    @Option @ToggleSetting(name = "Enable Fortnite Compass", category = FNCOMPASS)
    public static boolean FNCOMPASS_ENABLED = false;

    @Option @ToggleSetting(name = "Show Background", category = FNCOMPASS)
    public static boolean FNCOMPASS_BACKGROUND = true;

    @Option @ToggleSetting(name = "Enable Chroma", category = FNCOMPASS)
    public static boolean FNCOMPASS_CHROMA = false;

    @Option @ToggleSetting(name = "Enable Text Shadow", category = FNCOMPASS)
    public static boolean FNCOMPASS_SHADOW = true;

    @Option
    @SelectorSetting(category = FNCOMPASS, name = "Detail Level", items = {"0", "1", "2"})
    public static String FNCOMPASS_DETAILS = "2";

    
    
    @Option @ToggleSetting(name = "Enable Item Physics")
    public static boolean ITEM_PHYSIC_ENABLED = false;

    @Option @SelectorSetting(name = "Button Style", items = {"HYPERIUM", "HYPERIUM 2"})
    public static String BUTTON_STYLE = ButtonStyle.HYPERIUM.name();

    @Option @ToggleSetting(name = "Chroma Buttons (For Hyperium 2 Style)")
    public static boolean H2_BUTTONS_CHROMA = false;

    public static void register() {
        Hyperium.CONFIG.register(INSTANCE);
    }

    public static void save() {
        Hyperium.CONFIG.save();
    }
}
