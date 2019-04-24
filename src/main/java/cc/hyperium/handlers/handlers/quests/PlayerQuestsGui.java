package cc.hyperium.handlers.handlers.quests;

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.Icons;
import cc.hyperium.handlers.handlers.stats.AbstractHypixelStats;
import cc.hyperium.handlers.handlers.stats.display.StatsDisplayItem;
import cc.hyperium.mixinsimp.client.GlStateModifier;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.RenderUtils;
import club.sk1er.website.api.requests.HypixelApiGuild;
import club.sk1er.website.api.requests.HypixelApiPlayer;
import club.sk1er.website.utils.WebsiteUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerQuestsGui extends HyperiumGui {
    private HypixelApiPlayer player;
    private AbstractHypixelStats hovered;
    private AbstractHypixelStats focused;
    private ConcurrentHashMap<AbstractHypixelStats, GuiBlock> location = new ConcurrentHashMap<>();
    HypixelApiGuild guild;

    public PlayerQuestsGui(HypixelApiPlayer player) {
        this.player = player;

        guild = player.getGuild();
    }

    public static void print(ScaledResolution current, List<StatsDisplayItem> deepStats, int printY) {
        for (StatsDisplayItem statsDisplayItem : deepStats) {
            GlStateManager.pushMatrix();
            GlStateManager.resetColor();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int y = (95) + printY;
            if (y > 73 + 64 && y < current.getScaledHeight() - 50)
                statsDisplayItem.draw(current.getScaledWidth() / 2 - statsDisplayItem.width / 2, y);
            printY += statsDisplayItem.height;
            GlStateManager.popMatrix();
        }
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
        if (!flag && flag2) focused = null;

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
    protected void pack() {}

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution current = ResolutionUtil.current();
        boolean isInGuild = guild.isLoaded() && guild.isValid();
        double leftTextScale = 1.25;
        drawScaledText(player.getDisplayString() + (isInGuild ? " " + guild.getFormatedTag() : ""), 5, 10, leftTextScale, Color.WHITE.getRGB(), true, false);
        String s1 = "Quests Completed: ";
        drawScaledText(s1, 5, 55, leftTextScale, Color.CYAN.getRGB(), true, false);
        drawScaledText(WebsiteUtils.comma(player.getTotalQuests()), (int) (5 + fontRendererObj.getStringWidth(s1) * leftTextScale), 55, leftTextScale, Color.YELLOW.getRGB(), true, false);

        if (focused == null) {
            float scaleMod = 4 / 5F;
            GlStateManager.scale(scaleMod, scaleMod, scaleMod);
            final int blockWidth = 64 + 32;
            int blocksPerLine = (int) (current.getScaledWidth() / (1.2D * blockWidth));
            if (blocksPerLine % 2 == 1) {
                blocksPerLine--;
            }
            hovered = null;

            GlStateManager.scale(1 / scaleMod, 1 / scaleMod, 1 / scaleMod);

            if (hovered != null) {
                List<StatsDisplayItem> preview = hovered.getQuests(player);
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

                float scale = 1.25F;
                GlStateManager.scale(scale, scale, scale);
                int left = block.getRight() - xOffset + yRenderOffset;
                int top = block.getTop();
                int printY = 0;
                if (top + height * 2 > current.getScaledHeight()) {
                    top = current.getScaledHeight() - height * 2 - 50;
                }
                left = 5;
                top = 80;
                RenderUtils.drawRect((left - 3) / scale, (top - 3) / scale, (left + (width + 3) * scale) / scale, (top + (height + 3) * scale) / scale,
                    new Color(0, 0, 0, 175).getRGB());

                for (StatsDisplayItem statsDisplayItem : preview) {
                    statsDisplayItem.draw((int) (left / scale), (int) ((top) / scale) + printY);
                    printY += statsDisplayItem.height;
                }
                GlStateManager.scale(1 / scale, 1 / scale, 1 / scale);
            }
        } else {
            List<StatsDisplayItem> deepStats = focused.getQuests(player);
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

            print(current, deepStats, printY);
        }

        float totalDaily = 0;
        float completedDaily = 0;
        float totalWeekly = 0;
        float completedWeekly = 0;
        totalDaily = 1;
        totalWeekly = 1;

        float dailyPercent = Math.round(completedDaily / totalDaily * 100F);
        String s = "Daily Quests: ";
        drawScaledText(s, 5, 25, leftTextScale, Color.CYAN.getRGB(), true, false);
        drawScaledText((int) completedDaily + "/" + (int) totalDaily + " (" + (int) dailyPercent + "%)", (int) (5 + fontRendererObj.getStringWidth(s) * leftTextScale), 25, leftTextScale, Color.HSBtoRGB(dailyPercent / 100F / 3F, 1.0F, 1.0F), true, false);

        float weeklyPercent = Math.round(completedWeekly / totalWeekly * 100F);
        String text = "Weekly Quests: ";
        drawScaledText(text, 5, 40, leftTextScale, Color.CYAN.getRGB(), true, false);
        drawScaledText((int) completedWeekly + "/" + (int) totalWeekly + " (" + (int) weeklyPercent + "%)", (int) (5 + fontRendererObj.getStringWidth(text) * leftTextScale), 40, leftTextScale, Color.HSBtoRGB(weeklyPercent / 100F / 3F, 1.0F, 1.0F), true, false);
    }
}
