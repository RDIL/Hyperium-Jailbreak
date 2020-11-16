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

package cc.hyperium.mods.levelhead.config;

import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.SliderSetting;
import cc.hyperium.config.ToggleSetting;
import cc.hyperium.config.provider.IOptionSetProvider;
import cc.hyperium.utils.ChatColor;
import rocks.rdil.simpleconfig.Option;

public class LevelheadConfig implements IOptionSetProvider {
    public static final LevelheadConfig INSTANCE = new LevelheadConfig();

    @Override
    public String getName() {
        return "Levelhead";
    }

    @Option @ToggleSetting(name = "Enable")
    public static boolean ENABLED = true;

    @Option @ToggleSetting(name = "Show Self")
    public boolean SHOW_OWN = true;

    @Option @SliderSetting(name = "Render Distance", min = 5, max = 64, isInt = true)
    public int RENDER_DISTANCE = 64;

    @ConfigOpt @SliderSetting(name = "Cache Size", min = 150, max = 5000, isInt = true)
    public static int PURGE_SIZE = 500;

    @Option public boolean headerChroma = false;

    @Option public boolean headerRgb = false;

    @Option public String headerColor = ChatColor.AQUA.toString();

    @Option public int headerRed = 255;

    @Option public int headerGreen = 255;

    @Option public int headerBlue = 250;

    @Option public double headerAlpha = 1.0;

    @Option public String customHeader = "Level";

    @Option public boolean footerChroma = false;

    @Option public boolean footerRgb = false;

    @Option public String footerColor = ChatColor.YELLOW.toString();

    @Option public int footerRed = 255;

    @Option public int footerGreen = 255;

    @Option public int footerBlue = 250;

    @Option public double footerAlpha = 1.0;
}
