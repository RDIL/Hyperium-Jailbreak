package cc.hyperium.mixins.gui;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.gui.container.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer extends GuiScreen {
    @Shadow protected abstract boolean checkHotbarKeys(int keyCode);
    @Shadow protected abstract void drawSlot(Slot slotIn);
    @Shadow protected abstract boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY);
    @Shadow protected abstract void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY);
    @Shadow protected abstract void drawGuiContainerForegroundLayer(int mouseX, int mouseY);
    @Shadow protected abstract void drawItemStack(ItemStack stack, int x, int y, String altText);
    @Shadow private ItemStack draggedStack;
    @Shadow public Container inventorySlots;
    @Shadow protected int guiLeft;
    @Shadow protected int guiTop;
    @Shadow private int touchUpX;
    @Shadow private int touchUpY;
    @Shadow private Slot theSlot;
    @Shadow private Slot returningStackDestSlot;
    @Shadow private ItemStack returningStack;
    @Shadow @Final protected Set<Slot> dragSplittingSlots;
    @Shadow protected boolean dragSplitting;
    @Shadow private boolean isRightMouseClick;
    @Shadow private int dragSplittingRemnant;
    @Shadow private long returningStackTime;

    @Inject(method = "keyTyped", at = @At("HEAD"))
    public void keyTyped(char typedChar, int keyCode, CallbackInfo ci) {
        EventBus.INSTANCE.post(new GuiContainerKeyTypedHead(keyCode));
    }

    @Inject(method = "keyTyped", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/inventory/GuiContainer;checkHotbarKeys(I)Z", shift = At.Shift.BEFORE), cancellable = true)
    public void keyTyped$(char typedChar, int keyCode, CallbackInfo ci) {
        final GuiContainerKeyTypedPreHBKeysEvent e = new GuiContainerKeyTypedPreHBKeysEvent((GuiContainer) (Object) this, keyCode, this.theSlot);
        EventBus.INSTANCE.post(e);
        if (e.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "onGuiClosed", at = @At("HEAD"))
    private void onGuiClosed(CallbackInfo ci) {
        super.onGuiClosed();
    }

    @Inject(method = "mouseClicked", at = @At("RETURN"))
    private void mouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        checkHotbarKeys(mouseButton - 100);
    }

    /**
     * @author Reece Dunham and Mojang
     *
     * @param mouseX The mouse x value.
     * @param mouseY The mouse y value.
     * @param partialTicks The partialTicks value.
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        int i = this.guiLeft;
        int j = this.guiTop;
        this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        super.drawScreen(mouseX, mouseY, partialTicks);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) i, (float) j, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        this.theSlot = null;
        EventBus.INSTANCE.post(new SetLastSlotEvent());
        int k = 240;
        int l = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k / 1.0F, (float)l / 1.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1) {
            Slot slot = this.inventorySlots.inventorySlots.get(i1);
            EventBus.INSTANCE.post(new GuiContainerDrawSlotEvent((GuiContainer) (Object) this, slot));
            this.drawSlot(slot);

            if (this.isMouseOverSlot(slot, mouseX, mouseY) && slot.canBeHovered()) {
                this.theSlot = slot;
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                int j1 = slot.xDisplayPosition;
                int k1 = slot.yDisplayPosition;
                GlStateManager.colorMask(true, true, true, false);
                EventBus.INSTANCE.post(new GuiContainerDrawGradientRectEvent((GuiContainer) (Object) this, j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433, slot));
                this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
                GlStateManager.colorMask(true, true, true, true);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }

        RenderHelper.disableStandardItemLighting();
        this.drawGuiContainerForegroundLayer(mouseX, mouseY);
        RenderHelper.enableGUIStandardItemLighting();
        InventoryPlayer inventoryplayer = this.mc.thePlayer.inventory;
        ItemStack itemstack = this.draggedStack == null ? inventoryplayer.getItemStack() : this.draggedStack;

        if (itemstack != null) {
            int j2 = 8;
            int k2 = this.draggedStack == null ? 8 : 16;
            String s = null;

            if (this.draggedStack != null && this.isRightMouseClick) {
                itemstack = itemstack.copy();
                itemstack.stackSize = MathHelper.ceiling_float_int((float) itemstack.stackSize / 2.0F);
            } else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
                itemstack = itemstack.copy();
                itemstack.stackSize = this.dragSplittingRemnant;

                if (itemstack.stackSize == 0) {
                    s = "" + EnumChatFormatting.YELLOW + "0";
                }
            }

            this.drawItemStack(itemstack, mouseX - i - j2, mouseY - j - k2, s);
        }

        if (this.returningStack != null) {
            float f = (float)(Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;

            if (f >= 1.0F) {
                f = 1.0F;
                this.returningStack = null;
            }

            int l2 = this.returningStackDestSlot.xDisplayPosition - this.touchUpX;
            int i3 = this.returningStackDestSlot.yDisplayPosition - this.touchUpY;
            int l1 = this.touchUpX + (int)((float)l2 * f);
            int i2 = this.touchUpY + (int)((float)i3 * f);
            this.drawItemStack(this.returningStack, l1, i2, null);
        }

        GlStateManager.popMatrix();

        if (inventoryplayer.getItemStack() == null && this.theSlot != null && this.theSlot.getHasStack()) {
            ItemStack itemstack1 = this.theSlot.getStack();
            this.renderToolTip(itemstack1, mouseX, mouseY);
        }

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        EventBus.INSTANCE.post(new GuiContainerFinishDrawScreenEvent((GuiContainer) (Object) this, mouseX, mouseY, partialTicks));
    }
}
