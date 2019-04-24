package cc.hyperium.handlers.handlers.stats;

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.Icons;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.handlers.handlers.quests.PlayerQuestsGui;
import cc.hyperium.handlers.handlers.stats.display.StatsDisplayItem;
import cc.hyperium.mixinsimp.client.GlStateModifier;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.RenderUtils;
import club.sk1er.website.api.requests.HypixelApiGuild;
import club.sk1er.website.api.requests.HypixelApiPlayer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerStatsGui extends HyperiumGui {
    private HypixelApiPlayer player;
    private AbstractHypixelStats hovered;
    private AbstractHypixelStats focused;

    private ConcurrentHashMap<AbstractHypixelStats, GuiBlock> location = new ConcurrentHashMap<>();

    public PlayerStatsGui(HypixelApiPlayer player) {
        this.player = player;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean flag = false;
        for (GuiButton guiButton : buttonList) {
            if (guiButton.isMouseOver())
                flag = true;
        }
        boolean flag2 = focused == null;
        if (!flag && flag2)
            focused = null;

        ScaledResolution current = ResolutionUtil.current();
        if (focused != null && new GuiBlock((current.getScaledWidth() / 2 - 22 - 64), (current.getScaledWidth() / 2 - 22), 73, 73 + 64).isMouseOver(mouseX, mouseY)) {
            focused = null;
            offset = 0;
        }
        if (flag2)
            if (mouseButton == 0) {
                for (AbstractHypixelStats abstractHypixelStats : location.keySet()) {
                    if (location.get(abstractHypixelStats).isMouseOver(mouseX, mouseY)) {
                        focused = abstractHypixelStats;
                        hovered = null;
                        offset = 0;
                    }
                }
            }

    }

    @Override
    protected void pack() {
        reg("VIEW_GUILD", new GuiButton(nextId(), 1, 22, "View Guild"), button -> new GuildStatsGui(player.getGuild()).show(), button -> button.visible = player.getGuild().isLoaded() && player.getGuild().isValid());
        reg("VIEW_FRIENDS", new GuiButton(nextId(), 1, 22 + 21, "View Friends"), button -> {}, button -> { });
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution current = ResolutionUtil.current();
        HypixelApiGuild guild = player.getGuild();
        if (guild == null) {
            GeneralChatHandler.instance().sendMessage("Player not found!");
            mc.displayGuiScreen(null);
            return;
        }
        boolean isInGuild = guild.isLoaded() && guild.isValid();
        drawScaledText(player.getDisplayString() + (isInGuild ? " " + guild.getFormatedTag() : ""), current.getScaledWidth() / 2, 30, 3, Color.WHITE.getRGB(), true, true);
        if (focused == null) {
            final int blockWidth = 64 + 32;
            int blocksPerLine = (int) (current.getScaledWidth() / (1.2D * blockWidth));
            if (blocksPerLine % 2 == 1) {
                blocksPerLine--;
            }

            hovered = null;
            if (hovered != null) {
                List<StatsDisplayItem> preview = hovered.getPreview(player);
                int width = 0;
                int height = 0;
                for (StatsDisplayItem statsDisplayItem : preview) {
                    width = Math.max(width, statsDisplayItem.width);
                    height += statsDisplayItem.height;
                }
                GuiBlock block = location.get(hovered);
                int xOffset = 0;
                int yRenderOffset = 16;
                int rightSide = block.getRight() + (width + yRenderOffset) * 2;
                if (rightSide > current.getScaledWidth()) {
                    xOffset = rightSide - current.getScaledWidth();
                }
                float scale = 2.0F;
                GlStateManager.scale(scale, scale, scale);
                int left = block.getRight() - xOffset + yRenderOffset;
                int top = block.getTop();
                int printY = 0;
                if (top + height * 2 > current.getScaledHeight()) {
                    top = current.getScaledHeight() - height * 2 - 50;
                }
                RenderUtils.drawRect((left - 3) / scale, (top - 3) / scale, (left + (width + 3) * scale) / scale, (top + (height + 3) * scale) / scale,
                    new Color(0, 0, 0, 175).getRGB());

                for (StatsDisplayItem statsDisplayItem : preview) {
                    statsDisplayItem.draw((int) (left / scale), (int) ((top) / scale) + printY);
                    printY += statsDisplayItem.height;
                }
                GlStateManager.scale(1 / scale, 1 / scale, 1 / scale);
            }
        } else {
            List<StatsDisplayItem> deepStats = focused.getDeepStats(player);

            GlStateManager.resetColor();
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.translate(current.getScaledWidth() / 2 - 24, 80, 0);
            GlStateManager.scale(.2, .2, .2);
            drawTexturedModalRect(0, 0, 0, 0, 128 * 2, 128 * 2);
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            drawScaledText(focused.getName(), current.getScaledWidth() / 2, 60, 2.0, Color.RED.getRGB(), true, true);
            Icons.EXTENSION.bind();
            GlStateModifier.INSTANCE.reset();
            Icons.EXIT.bind();
            float scale = 4.0F;
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.translate(current.getScaledWidth() / 2 / scale - 90 / scale, (73) / scale, 0);
            GlStateManager.rotate(180, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(-16, -16, 0);
            drawScaledCustomSizeModalRect(0, 0, 0, 0, 64, 64, 16, 16, 64, 64);
            GlStateManager.popMatrix();
            int printY = 55 - offset;

            PlayerQuestsGui.print(current, deepStats, printY);
        }
    }
}
