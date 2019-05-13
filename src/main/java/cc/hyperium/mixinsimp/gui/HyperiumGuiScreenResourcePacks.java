package cc.hyperium.mixinsimp.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiResourcePackAvailable;
import net.minecraft.client.gui.GuiResourcePackSelected;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.ResourcePackListEntry;
import java.util.List;

public class HyperiumGuiScreenResourcePacks {
    private GuiScreenResourcePacks parent;
    private GuiResourcePackAvailable availableResourcePacksList;

    public HyperiumGuiScreenResourcePacks(GuiScreenResourcePacks parent) {
        this.parent = parent;
    }

    public void initGui(List<GuiButton> buttonList) {
        buttonList.forEach(b -> {
            b.setWidth(200);
            if (b.id == 2) {
                b.xPosition = parent.width / 2 - 204;
            }
        });
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}

    public GuiResourcePackAvailable updateList(List<ResourcePackListEntry> availableResourcePacks, Minecraft mc, int height, int width) {
        this.availableResourcePacksList = new GuiResourcePackAvailable(mc, 200, height,availableResourcePacks);
        this.availableResourcePacksList.setSlotXBoundsFromLeft(width / 2 - 4 - 200);
        this.availableResourcePacksList.registerScrollButtons(7, 8);

        return availableResourcePacksList;
    }

    public void drawScreen(GuiResourcePackAvailable availableResourcePacksList, GuiResourcePackSelected selectedResourcePacksList, int mouseX, int mouseY, float partialTicks, FontRenderer fontRendererObj, int width) {
        parent.drawBackground(0);
        availableResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        selectedResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
        parent.drawCenteredString(fontRendererObj, I18n.format("resourcePack.title"), width / 2,16, 16777215);
    }
}
