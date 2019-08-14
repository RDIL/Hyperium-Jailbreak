package cc.hyperium.gui.hyperium;
import cc.hyperium.Hyperium;
import cc.hyperium.config.Category;
import cc.hyperium.config.Settings;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.Icons;
import cc.hyperium.gui.MaterialTextField;
import cc.hyperium.gui.hyperium.components.AbstractTab;
import cc.hyperium.gui.hyperium.tabs.SettingsTab;
import cc.hyperium.handlers.handlers.SettingsHandler;
import cc.hyperium.mixinsimp.client.GlStateModifier;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class HyperiumMainGui extends HyperiumGui {
    public static HyperiumMainGui INSTANCE = new HyperiumMainGui();
    private static int tabIndex = 0; // save tab position
    public boolean show = false;
    private int initialGuiScale;
    private HashMap<Field, Supplier<String[]>> customStates = new HashMap<>();
    private HashMap<Field, List<Consumer<Object>>> callbacks = new HashMap<>();
    private List<Object> settingsObjects = new ArrayList<>();
    private HyperiumFontRenderer smol;
    private HyperiumFontRenderer font;
    private HyperiumFontRenderer title;
    private List<AbstractTab> tabs;
    private AbstractTab currentTab;
    private List<RGBFieldSet> rgbFields = new ArrayList<>();
    private MaterialTextField searchField;


    private HyperiumMainGui() {
        smol = new HyperiumFontRenderer(Settings.GUI_FONT, 14.0F, 0, 1.0F);
        font = new HyperiumFontRenderer(Settings.GUI_FONT, 16.0F, 0, 1.0F);
        title = new HyperiumFontRenderer(Settings.GUI_FONT, 30.0F, 0, 1.0F);
        settingsObjects.add(Settings.INSTANCE);
        settingsObjects.add(Hyperium.INSTANCE.getModIntegration().getAutotip());
        settingsObjects.add(Hyperium.INSTANCE.getModIntegration().getAutoGG().getConfig());
        settingsObjects.add(Hyperium.INSTANCE.getModIntegration().getLevelhead().getConfig());
        settingsObjects.add(Hyperium.INSTANCE.getModIntegration().getGlintcolorizer().getColors());
        SettingsHandler settingsHandler = Hyperium.INSTANCE.getHandlers().getSettingsHandler();
        settingsObjects.addAll(settingsHandler.getSettingsObjects());
        HashMap<Field, List<Consumer<Object>>> call1 = settingsHandler.getcallbacks();
        for (Field field : call1.keySet()) {
            callbacks.computeIfAbsent(field, tmp -> new ArrayList<>()).addAll(call1.get(field));
        }

        HashMap<Field, Supplier<String[]>> customStates = settingsHandler.getCustomStates();
        for (Field field : customStates.keySet()) {
            this.customStates.put(field, customStates.get(field));
        }
        try {
            rgbFields.add(new RGBFieldSet(
                Settings.class.getDeclaredField("REACH_RED"),
                Settings.class.getDeclaredField("REACH_GREEN"),
                Settings.class.getDeclaredField("REACH_BLUE"), Category.REACH, true,
                Settings.INSTANCE));
            rgbFields.add(new RGBFieldSet(
                Settings.class.getDeclaredField("BUTTON_RED"),
                Settings.class.getDeclaredField("BUTTON_GREEN"),
                Settings.class.getDeclaredField("BUTTON_BLUE"), Category.BUTTONS, false,
                Settings.INSTANCE));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabs = Collections.singletonList(new SettingsTab(this));
        scollMultiplier = 2;
        setTab(tabIndex);
    }

    public HashMap<Field, Supplier<String[]>> getCustomStates() {
        return customStates;
    }

    public HashMap<Field, List<Consumer<Object>>> getCallbacks() {
        return callbacks;
    }

    public List<RGBFieldSet> getRgbFields() {
        return rgbFields;
    }

    public List<Object> getSettingsObjects() {
        return settingsObjects;
    }

    @Override
    protected void pack() {
        show = false;
        int yg = (height / 10); // Y grid
        int xg = (width / 11);  // X grid
        searchField = new MaterialTextField(xg * 10 - 110, yg + (yg / 2 - 10), 100, 20,
            I18n.format("tabs.searchbar"), font);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int yg = (height / 10); // Y grid
        int xg = (width / 11);  // X grid

        if (Minecraft.getMinecraft().theWorld == null) renderHyperiumBackground(ResolutionUtil.current());

        GlStateModifier.INSTANCE.reset();
        Icons.LIGHTBULB.bind();

        int w = yg * 88 / 144;
        int x = this.width / 2 - w - 10;
        int x1 = this.width / 2 + 10;

        drawRect(x - 3, 0, x + w + 3, yg, new Color(0, 0, 0, 50).getRGB());
        drawScaledCustomSizeModalRect(x, 0, 0, 0, 88, 128, w,
            yg, 88, 128);
        Icons.LIGHTBULB_SOLID.bind();
        drawRect(x1 - 3, 0, x1 + w + 3, yg, new Color(0, 0, 0, 50).getRGB());

        drawScaledCustomSizeModalRect(x1, 0, 0, 0, 88, 128, w,
            yg, 88, 128);
        if (Minecraft.getMinecraft().theWorld == null) {
            this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
            this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        }
        drawRect(xg, yg, xg * 10, yg * 2, new Color(0, 0, 0, Settings.SETTINGS_ALPHA).getRGB());
        drawRect(xg, yg * 2, xg * 10, yg * 9, new Color(0, 0, 0, Settings.SETTINGS_ALPHA / 2).getRGB());
        searchField.render(mouseX, mouseY);
        GlStateModifier.INSTANCE.reset();

        title.drawCenteredString(I18n.format(currentTab.getTitle()), this.width / 2,
            yg + (yg / 2 - 8), 0xFFFFFF);

        currentTab.setFilter(searchField.getText().isEmpty() ? null : searchField.getText());
        currentTab.render(xg, yg * 2, xg * 9, yg * 7);

        smol.drawString(Hyperium.version, this.width - smol.getWidth(Hyperium.version) - 1,
            height - 10, 0xffffffff);

        Icons.ARROW_LEFT.bind();
        GlStateManager.pushMatrix();
        Gui.drawScaledCustomSizeModalRect(this.width / 2 - xg, yg * 9, 0, 0, 144, 144, yg / 2, yg / 2,
            144, 144);
        Icons.ARROW_RIGHT.bind();
        Gui.drawScaledCustomSizeModalRect(this.width / 2 + xg - (yg / 2), yg * 9, 0, 0, 144, 144, yg / 2,
            yg / 2, 144, 144);
        GlStateManager.popMatrix();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        searchField.update();
    }

    @Override
    public void show() {
        // Set user's GUI scale to normal whilst the GUI is open.
        initialGuiScale = Minecraft.getMinecraft().gameSettings.guiScale;
        Minecraft.getMinecraft().gameSettings.guiScale = 2;
        super.show();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int yg = (height / 10); // Y grid
        int xg = (width / 11);  // X grid

        int w = yg * 88 / 144;
        int x = this.width / 2 - w - 10;
        int x1 = this.width / 2 + 10;

        if (mouseY > 0 && mouseY < yg) {
            int settingsAlpha = Settings.SETTINGS_ALPHA;
            if (mouseX >= x && mouseX <= x + w) {
                if (settingsAlpha >= 100) {
                    Settings.SETTINGS_ALPHA -= 25;
                }
            } else if (mouseX >= x + w && mouseX <= x1 + w) {
                if (settingsAlpha <= 225)
                    Settings.SETTINGS_ALPHA += 25;
            }
        }

        if (mouseY >= yg * 9 && mouseY <= yg * 10) {
            int size = tabs.size();
            int i = tabs.indexOf(currentTab);
            int ix = width / 2 - xg;
            if (mouseX >= ix && mouseX <= ix + xg / 2) {
                i--;
                if (i < 0) {
                    i = size - 1;
                }
                setTab(i);
            }
            ix = width / 2 + xg / 2;
            if (mouseX >= ix && mouseX <= ix + xg / 2) {
                i++;
                if (i > size - 1) {
                    i = 0;
                }
                setTab(i);
            }
        }

        searchField.onClick(mouseX, mouseY, mouseButton);
    }

    private void renderHyperiumBackground(ScaledResolution sr) {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableAlpha();

        Minecraft.getMinecraft().getTextureManager().bindTexture(background);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0D, sr.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
        worldrenderer.pos(sr.getScaledWidth(), sr.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
        worldrenderer.pos(sr.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();

        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public HyperiumFontRenderer getFont() {
        return font;
    }

    public void setTab(int i) {
        tabIndex = i;
        currentTab = tabs.get(i);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Hyperium.CONFIG.save();
        Minecraft.getMinecraft().gameSettings.guiScale = initialGuiScale;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        currentTab.handleMouseInput();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        searchField.keyTyped(typedChar, keyCode);
    }
}
