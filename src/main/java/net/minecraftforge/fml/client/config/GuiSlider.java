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

package net.minecraftforge.fml.client.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

/**
 * This class is borrowed from iChunUtils with permission.
 *
 * @author iChun
 */
public class GuiSlider extends GuiButtonExt {
    /**
     * The value of this slider control.
     */
    public double sliderValue = 1.0F;
    /**
     * The {@link java.lang.String} to display.
     */
    public String dispString;
    /**
     * If the slider is being dragged or not.
     */
    public boolean dragging = false;
    /**
     * If decimals should be shown.
     */
    public boolean showDecimal = true;
    /**
     * The minimum value of the slider.
     */
    public double minValue = 0.0D;
    /**
     * The maximum value of the slider.
     */
    public double maxValue = 5.0D;
    /**
     * The precision of the slider.
     */
    public int precision = 1;

    /**
     * The parent slider.
     */
    public ISlider parent;

    /**
     * The suffix.
     */
    public String suffix;

    /**
     * If the {@link java.lang.String} should be drawn or not.
     */
    public boolean drawString = true;

    /**
     * Creates a new slider instance.
     *
     * @param id The slider's unique ID.
     * @param xPos The x position of the slider.
     * @param yPos The y position of the slider.
     * @param width The width of the slider.
     * @param height The height of the slider.
     * @param prefix The prefix of the slider.
     * @param suf The suffix of the slider.
     * @param minVal The minimum value of the slider.
     * @param maxVal The maximum value of the slider.
     * @param currentVal The current value of the slider.
     * @param showDec If decimals should be shown or not.
     * @param drawStr If the string should be drawn or not.
     */
    public GuiSlider(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr) {
        this(id, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, null);
    }

    /**
     * Creates a new slider instance.
     *
     * @param id The slider's unique ID.
     * @param xPos The x position of the slider.
     * @param yPos The y position of the slider.
     * @param width The width of the slider.
     * @param height The height of the slider.
     * @param prefix The prefix of the slider.
     * @param suf The suffix of the slider.
     * @param minVal The minimum value of the slider.
     * @param maxVal The maximum value of the slider.
     * @param currentVal The current value of the slider.
     * @param showDec If decimals should be shown or not.
     * @param drawStr If the string should be drawn or not.
     * @param par The slider.
     */
    public GuiSlider(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, ISlider par) {
        super(id, xPos, yPos, width, height, prefix);
        minValue = minVal;
        maxValue = maxVal;
        sliderValue = (currentVal - minValue) / (maxValue - minValue);
        dispString = prefix;
        parent = par;
        suffix = suf;
        showDecimal = showDec;
        String val;

        if (showDecimal) {
            val = Double.toString(sliderValue * (maxValue - minValue) + minValue);
            precision = Math.min(val.substring(val.indexOf(".") + 1).length(), 4);
        } else {
            val = Integer.toString((int) Math.round(sliderValue * (maxValue - minValue) + minValue));
            precision = 0;
        }

        displayString = dispString + val + suffix;

        drawString = drawStr;
        if (!drawString) displayString = "";
    }

    @Override
    public int getHoverState(boolean par1) {
        return 0;
    }

    @Override
    protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3) {
        if (this.visible) {
            if (this.dragging) {
                this.sliderValue = (par2 - (this.xPosition + 4)) / (float) (this.width - 8);
                updateSlider();
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    @Override
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
        if (super.mousePressed(par1Minecraft, par2, par3)) {
            this.sliderValue = (float) (par2 - (this.xPosition + 4)) / (float) (this.width - 8);
            updateSlider();
            this.dragging = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Updates the slider.
     */
    public void updateSlider() {
        if (this.sliderValue < 0.0F) {
            this.sliderValue = 0.0F;
        }

        if (this.sliderValue > 1.0F) {
            this.sliderValue = 1.0F;
        }

        StringBuilder val;

        if (showDecimal) {
            val = new StringBuilder(Double.toString(sliderValue * (maxValue - minValue) + minValue));

            if (val.substring(val.indexOf(".") + 1).length() > precision) {
                val = new StringBuilder(val.substring(0, val.indexOf(".") + precision + 1));

                if (val.toString().endsWith(".")) {
                    val = new StringBuilder(val.substring(0, val.indexOf(".") + precision));
                }
            } else {
                while (val.substring(val.indexOf(".") + 1).length() < precision) {
                    val.append("0");
                }
            }
        } else {
            val = new StringBuilder(Integer.toString((int) Math.round(sliderValue * (maxValue - minValue) + minValue)));
        }

        if (drawString) {
            displayString = dispString + val + suffix;
        }

        if (parent != null) {
            parent.onChangeSliderValue(this);
        }
    }

    @Override
    public void mouseReleased(int par1, int par2) {
        this.dragging = false;
    }

    /**
     * Get the value of the slider as an integer.
     *
     * @return The value of the slider as an integer.
     */
    public int getValueInt() {
        return (int) Math.round(sliderValue * (maxValue - minValue) + minValue);
    }

    /**
     * Get the value of the slider.
     *
     * @return The value of the slider.
     */
    public double getValue() {
        return sliderValue * (maxValue - minValue) + minValue;
    }

    /**
     * Set the value of the slider.
     *
     * @param d The new value of the slider.
     */
    public void setValue(double d) {
        this.sliderValue = (d - minValue) / (maxValue - minValue);
    }

    /**
     * A slider.
     */
    public interface ISlider {
        void onChangeSliderValue(GuiSlider slider);
    }
}