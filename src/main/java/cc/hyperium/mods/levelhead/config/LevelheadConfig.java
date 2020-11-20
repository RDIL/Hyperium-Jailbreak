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

import cc.hyperium.config.Category;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.SliderSetting;
import cc.hyperium.config.ToggleSetting;
import cc.hyperium.utils.ChatColor;

public class LevelheadConfig {
    @ConfigOpt @ToggleSetting(category = Category.LEVEL_HEAD, mods = true, name = "Enable")
    public boolean enabled = true;

    @ConfigOpt @ToggleSetting(category = Category.LEVEL_HEAD, mods = true, name = "Show Self")
    public boolean showSelf = true;

    @ConfigOpt @SliderSetting(name = "Render Distance", mods = true, category = Category.LEVEL_HEAD, min = 5, max = 64, isInt = true)
    public int renderDistance = 64;

    @ConfigOpt @SliderSetting(name = "Cache Size", mods = true, category = Category.LEVEL_HEAD, min = 150, max = 5000, isInt = true)
    public int purgeSize = 500;

    @ConfigOpt public boolean headerChroma = false;

    @ConfigOpt public boolean headerRgb = false;

    @ConfigOpt public String headerColor = ChatColor.AQUA.toString();

    @ConfigOpt public int headerRed = 255;

    @ConfigOpt public int headerGreen = 255;

    @ConfigOpt public int headerBlue = 250;

    @ConfigOpt public double headerAlpha = 1.0;

    @ConfigOpt public String customHeader = "Level";

    @ConfigOpt public boolean footerChroma = false;

    @ConfigOpt public boolean footerRgb = false;

    @ConfigOpt public String footerColor = ChatColor.YELLOW.toString();

    @ConfigOpt public int footerRed = 255;

    @ConfigOpt public int footerGreen = 255;

    @ConfigOpt public int footerBlue = 250;

    @ConfigOpt public double footerAlpha = 1.0;

    public boolean isFooterChroma() {
        return footerChroma;
    }

    public void setFooterChroma(boolean footerChroma) {
        this.footerChroma = footerChroma;
    }

    public boolean isFooterRgb() {
        return footerRgb;
    }

    public void setFooterRgb(boolean footerRgb) {
        this.footerRgb = footerRgb;
    }

    public String getFooterColor() {
        return footerColor;
    }

    public void setFooterColor(String footerColor) {
        this.footerColor = footerColor;
    }

    public int getFooterRed() {
        return footerRed;
    }

    public void setFooterRed(int footerRed) {
        this.footerRed = footerRed;
    }

    public int getFooterGreen() {
        return footerGreen;
    }

    public void setFooterGreen(int footerGreen) {
        this.footerGreen = footerGreen;
    }

    public int getFooterBlue() {
        return footerBlue;
    }

    public void setFooterBlue(int footerBlue) {
        this.footerBlue = footerBlue;
    }

    public double getFooterAlpha() {
        return footerAlpha;
    }

    public boolean isHeaderChroma() {
        return headerChroma;
    }

    public void setHeaderChroma(boolean headerChroma) {
        this.headerChroma = headerChroma;
    }

    public boolean isHeaderRgb() {
        return headerRgb;
    }

    public void setHeaderRgb(boolean headerRgb) {
        this.headerRgb = headerRgb;
    }

    public String getHeaderColor() {
        return headerColor;
    }

    public void setHeaderColor(String headerColor) {
        this.headerColor = headerColor;
    }

    public int getHeaderRed() {
        return headerRed;
    }

    public void setHeaderRed(int headerRed) {
        this.headerRed = headerRed;
    }

    public int getHeaderGreen() {
        return headerGreen;
    }

    public void setHeaderGreen(int headerGreen) {
        this.headerGreen = headerGreen;
    }

    public int getHeaderBlue() {
        return headerBlue;
    }

    public void setHeaderBlue(int headerBlue) {
        this.headerBlue = headerBlue;
    }

    public double getHeaderAlpha() {
        return headerAlpha;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getRenderDistance() {
        return renderDistance;
    }

    public void setRenderDistance(int renderDistance) {
        this.renderDistance = renderDistance;
    }

    public boolean isShowSelf() {
        return showSelf;
    }

    public void setShowSelf(boolean showSelf) {
        this.showSelf = showSelf;
    }

    public int getPurgeSize() {
        return purgeSize;
    }

    public void setPurgeSize(int purgeSize) {
        this.purgeSize = purgeSize;
    }

    public String getCustomHeader() {
        return customHeader;
    }

    public void setCustomHeader(String customHeader) {
        this.customHeader = customHeader;
    }
}
