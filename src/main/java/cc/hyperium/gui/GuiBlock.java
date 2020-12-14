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

package cc.hyperium.gui;

import net.minecraft.client.gui.FontRenderer;
import java.util.List;

/**
 * A GUI block.
 */
public class GuiBlock {
    private int left;
    private int right;
    private int top;
    private int bottom;

    /**
     * Creates a new GUI block instance with the specified dimensions.
     *
     * @param left The left value.
     * @param right The right value.
     * @param top The top value.
     * @param bottom The bottom value.
     */
    public GuiBlock(int left, int right, int top, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    /**
     * Returns a String representation of this GUI block.
     *
     * @return The GUI block as a String.
     */
    @Override
    public String toString() {
        return "GuiBlock{left=" + this.left + ", right=" + this.right + ", top=" + this.top + ", bottom=" + this.bottom + '}';
    }

    /**
     * Get the GUI block's width.
     *
     * @return The GUI block's width.
     */
    public int getWidth() {
        return this.right - this.left;
    }

    /**
     * Get the GUI block's height.
     *
     * @return The GUI block's height.
     */
    public int getHeight() {
        return this.bottom - this.top;
    }

    /**
     * Get the GUI block's left value.
     *
     * @return The GUI block's left value.
     */
    public int getLeft() {
        return left;
    }

    /**
     * Get the GUI block's right value.
     *
     * @return The GUI block's right value.
     */
    public int getRight() {
        return right;
    }

    /**
     * Get the GUI block's top value.
     *
     * @return The GUI block's top value.
     */
    public int getTop() {
        return top;
    }

    /**
     * Get the GUI block's bottom value.
     *
     * @return The GUI block's bottom value.
     */
    public int getBottom() {
        return bottom;
    }

    /**
     * Set the GUI block's left value to the specified value.
     *
     * @param left The new left value.
     */
    public void setLeft(int left) {
        this.left = left;
    }

    /**
     * Set the GUI block's right value to the specified value.
     *
     * @param right The new right value.
     */
    public void setRight(int right) {
        this.right = right;
    }

    /**
     * Set the GUI block's top value to the specified value.
     *
     * @param top The new top value.
     */
    public void setTop(int top) {
        this.top = top;
    }

    /**
     * Set the GUI block's bottom value to the specified value.
     *
     * @param bottom The new bottom value.
     */
    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    /**
     * Get if the mouse is over this GUI block or not.
     *
     * @param x The mouse's x coordinate.
     * @param y The mouse's y coordinate.
     * @return If the mouse is over the GUI block or not.
     */
    public boolean isMouseOver(int x, int y) {
        return x >= this.left && x <= this.right && y >= this.top && y <= this.bottom;
    }

    /**
     * Calls {@link GuiBlock#drawString(String, FontRenderer, boolean, boolean, int, int, boolean, boolean, int, boolean)} on each of the Strings in the passed list.
     *
     * @param strings The Strings to draw.
     * @param fontRenderer The font renderer to use.
     * @param shadow If the text should have a shadow or not.
     * @param center If the text should be centered or not.
     * @param xOffset The x-offset of the text on-screen.
     * @param yOffset The y-offset of the text on-screen.
     * @param scaleToFitX If the text should be scaled to fit the x value.
     * @param scaleToFixY If the text should be scaled to fit the y value.
     * @param color The color to draw the text in.
     * @param sideLeft If the text should be left sided or not.
     * @return If all the Strings were drawn successfully or not.
     * @see GuiBlock#drawString(String, FontRenderer, boolean, boolean, int, int, boolean, boolean, int, boolean)
     */
    public boolean drawString(List<String> strings, FontRenderer fontRenderer, boolean shadow, boolean center, int xOffset, int yOffset, boolean scaleToFitX, boolean scaleToFixY, int color, boolean sideLeft) {
        boolean suc = true;
        for (String string : strings) {
            suc = suc && drawString(string, fontRenderer, shadow, center, xOffset, yOffset, scaleToFitX, scaleToFixY, color, sideLeft);
        }
        return suc;
    }

    /**
     * Moves the location of this block.
     *
     * @param x How far on the x-axis it should be moved.
     * @param y How far on the y-axis it should be moved.
     */
    public void translate(int x, int y) {
        this.left += x;
        this.right += x;
        this.top += y;
        this.bottom += y;
    }

    /**
     * Draws the specified String with the specified font renderer.
     *
     * @param string The String to draw.
     * @param fontRenderer The font renderer to use.
     * @param shadow If the text should have a shadow or not.
     * @param center If the text should be centered or not.
     * @param xOffset The x-offset of the text on-screen.
     * @param yOffset The y-offset of the text on-screen.
     * @param scaleToFitX If the text should be scaled to fit the x value.
     * @param scaleToFixY If the text should be scaled to fit the y value.
     * @param color The color to draw the text in.
     * @param sideLeft If the text should be left sided or not.
     * @return If the text was drawn successfully or not.
     */
    public boolean drawString(String string, FontRenderer fontRenderer, boolean shadow, boolean center, int xOffset, int yOffset, boolean scaleToFitX, boolean scaleToFixY, int color, boolean sideLeft) {
        int stringWidth = fontRenderer.getStringWidth(string);
        int x;

        if (sideLeft) {
            x = this.left + xOffset;
        } else {
            x = this.right - stringWidth - xOffset;
        }

        int y = this.top + yOffset;

        if (center) {
            x -= stringWidth / 2;
        }

        if (sideLeft) {
            if (x + stringWidth > this.right) {
                if (!scaleToFitX) {
                    return false;
                }

                this.right = x + stringWidth + xOffset;
            }
        } else if (this.right - stringWidth < this.left) {
            if (!scaleToFitX) {
                return false;
            }

            this.right = x + stringWidth + xOffset;
            x = this.right;
        }

        if (y + 10 > this.bottom) {
            if (!scaleToFixY) {
                return false;
            }
            this.bottom = y + 10;
        }

        if (y < this.top) {
            if (!scaleToFixY) {
                return false;
            }
            this.top = y;
        }

        fontRenderer.drawString(string, (float) x, (float) y, color, shadow);
        return true;
    }

    /**
     * Returns a new GUI block scaled to the specified value.
     *
     * @param scale The scale.
     * @return The new GUI block instance.
     */
    public GuiBlock multiply(double scale) {
        return new GuiBlock((int) (left * scale), (int) (right * scale), (int) (top * scale), (int) (bottom * scale));
    }
}
