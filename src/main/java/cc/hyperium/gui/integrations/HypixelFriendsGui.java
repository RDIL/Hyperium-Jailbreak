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

package cc.hyperium.gui.integrations;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.GuiBoxItem;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import net.hypixel.api.HypixelApiFriendObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HypixelFriendsGui extends HyperiumGui {
    private static FriendSortType sortType = FriendSortType.NONE;
    private final List<GuiBoxItem<HypixelApiFriendObject>> friendListBoxes = new ArrayList<>();
    private int tick;
    private HypixelFriends friends;
    private GuiTextField textField;
    private GuiBoxItem<HypixelApiFriendObject> selectedItem = null;
    private int columnWidth;

    public HypixelFriendsGui() {
        rebuildFriends();
        EventBus.INSTANCE.register(this);
    }

    @Override
    public void initGui() {
        columnWidth = fontRendererObj.getStringWidth("[YOUTUBER] Zyphalopagus1245");
        super.initGui();
    }

    protected void pack() {
        int textWidth = Math.max(ResolutionUtil.current().getScaledWidth() / 9, 100);
        int height = 20;
        if (textField == null)
            textField = new GuiTextField(nextId(), Minecraft.getMinecraft().fontRendererObj, ResolutionUtil.current().getScaledWidth() / 2 - textWidth / 2, 25, textWidth, height);

        reg("SORT", new GuiButton(nextId(), ResolutionUtil.current().getScaledWidth() - 153, 23, 150, 20, "Sort by: "), guiButton -> {
            int ord = sortType.ordinal();
            ord++;
            if (ord >= FriendSortType.values().length)
                ord = 0;
            sortType = FriendSortType.values()[ord];
            rebuildFriends();
            this.friends.sort(sortType);
        }, guiButton -> guiButton.displayString = "Sort by: " + sortType.getName());
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        textField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        textField.mouseClicked(mouseX, mouseY, mouseButton);
        selectedItem = null;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        tick++;
        if (tick % 20 == 0) {
            rebuildFriends();
        }
    }

    private void rebuildFriends() {
        try {
            this.friends = new HypixelFriends(Hyperium.INSTANCE.getHandlers().getDataHandler().getFriendsForCurrentUser().get().getData());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        this.friends.sort(sortType);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        friendListBoxes.clear();
        friends.removeIf(hypixelApiFriendObject -> !hypixelApiFriendObject.getDisplay().toLowerCase().contains(textField.getText().toLowerCase()));
        super.drawScreen(mouseX, mouseY, partialTicks);

        textField.drawTextBox();

        final int bottomRenderBound = ResolutionUtil.current().getScaledHeight() / 9 * 8;

        final int topRenderBound = 50;
        if (selectedItem != null) {
            GuiBlock box = selectedItem.getBox();
            int left = box.getLeft() - 2;
            int right = box.getLeft() + fontRendererObj.getStringWidth(selectedItem.getObject().getDisplay()) + 2;
            int top = box.getTop() - 2;
            int bottom = top + 10;
            if (top >= topRenderBound && bottom <= bottomRenderBound) {
                Gui.drawRect(left, top, right, top + 1, Color.RED.getRGB());
                Gui.drawRect(left, bottom, right, bottom + 1, Color.RED.getRGB());
                Gui.drawRect(right, top, right - 1, bottom, Color.RED.getRGB());
                Gui.drawRect(left, top, left + 1, bottom, Color.RED.getRGB());
            }
        }
        GuiBlock namesBlock = new GuiBlock(2, columnWidth, topRenderBound, topRenderBound);
        GuiBlock friendsBlock = new GuiBlock(namesBlock.getRight() + 15, ResolutionUtil.current().getScaledWidth() - 100, topRenderBound, bottomRenderBound);
        int drawX = friendsBlock.getLeft();
        int drawY = friendsBlock.getTop() - offset;
        if (drawY > bottomRenderBound) {
            offset = 0;
        }

        int cols = 1;
        while (drawX + columnWidth * cols < friendsBlock.getRight()) {
            cols++;
        }
        cols -= 1;
        if (cols <= 0)
            return;

        for (HypixelApiFriendObject object : friends.get()) {
            if (drawX + columnWidth > friendsBlock.getRight()) {
                drawX = friendsBlock.getLeft();
                drawY += 11;
            }
            if (selectedItem != null && selectedItem.getObject().equals(object)) {
                selectedItem = new GuiBoxItem<>(new GuiBlock(drawX, drawX + columnWidth, drawY + friendsBlock.getTop(), drawY + friendsBlock.getTop() + 11), object);
            }
            if (friendsBlock.drawString(object.getDisplay(), fontRendererObj, false, false, drawX - friendsBlock.getLeft(), drawY, false, false, Color.WHITE.getRGB(), true)) {
                GuiBoxItem<HypixelApiFriendObject> e = new GuiBoxItem<>(new GuiBlock(drawX, drawX + columnWidth, drawY + friendsBlock.getTop(), drawY + friendsBlock.getTop() + 11), object);
                friendListBoxes.add(e);
            }
            drawX += columnWidth;
        }

        // After first wave, if bottom of people is still not on screen, fix
        if (drawY < topRenderBound)
            offset = 0;
    }
}
