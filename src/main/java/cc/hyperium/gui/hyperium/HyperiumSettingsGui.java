package cc.hyperium.gui.hyperium;

import cc.hyperium.Hyperium;
import cc.hyperium.config.SelectorSetting;
import cc.hyperium.config.SliderSetting;
import cc.hyperium.config.ToggleSetting;
import cc.hyperium.config.provider.IOptionSetProvider;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.ScissorState;
import cc.hyperium.gui.hyperium.components.AbstractTabComponent;
import cc.hyperium.gui.hyperium.components.CollapsibleTabComponent;
import cc.hyperium.gui.hyperium.components.SelectorComponent;
import cc.hyperium.gui.hyperium.components.SliderComponent;
import cc.hyperium.gui.hyperium.components.ToggleComponent;
import cc.hyperium.mixinsimp.client.GlStateModifier;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.HyperiumFontRenderer;
import cc.hyperium.utils.SimpleAnimValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;
import rocks.rdil.simpleconfig.Option;

public class HyperiumSettingsGui extends HyperiumGui {
    public static final Logger LOGGER = LogManager.getLogger();
    public static HyperiumSettingsGui INSTANCE = new HyperiumSettingsGui();
    public boolean show = false;
    private int initialGuiScale;
    private final HashMap<Field, List<Consumer<Object>>> callbacks = new HashMap<>();
    private final List<IOptionSetProvider> settingsObjects = new ArrayList<>();
    private final HyperiumFontRenderer font;
    private final HyperiumFontRenderer title;
    protected List<AbstractTabComponent> components = new ArrayList<>();
    public Map<AbstractTabComponent, Boolean> clickStates = new HashMap<>();
    private SimpleAnimValue scrollAnim = new SimpleAnimValue(0L, 0f, 0f);
    private int scroll = 0;

    private HyperiumSettingsGui() {
        font = new HyperiumFontRenderer("OpenSans", 16.0F, 0, 1.0F);
        title = new HyperiumFontRenderer("OpenSans", 30.0F, 0, 1.0F);

        final List<IOptionSetProvider> actualProviders = new ArrayList<>();

        for (final Object o : Hyperium.CONFIG.getConfigObjs()) {
            // check for IOptionSetProvider#getName
            if (IOptionSetProvider.class.isAssignableFrom(o.getClass())) {
                actualProviders.add((IOptionSetProvider) o);
            }
        }

        settingsObjects.addAll(actualProviders);

        scollMultiplier = 2;
        HashMap<IOptionSetProvider, CollapsibleTabComponent> items = new HashMap<>();

        for (IOptionSetProvider o : this.getSettingsObjects()) {
            for (Field f : o.getClass().getDeclaredFields()) {
                final Option option = f.getAnnotation(Option.class);

                if (option != null) {
                    final ToggleSetting ts = f.getAnnotation(ToggleSetting.class);
                    final SelectorSetting ss = f.getAnnotation(SelectorSetting.class);
                    final SliderSetting sliderSetting = f.getAnnotation(SliderSetting.class);
                    List<Consumer<Object>> objectConsumer = this.getCallbacks().get(f);
                    AbstractTabComponent tabComponent = null;

                    if (ts != null) {
                        tabComponent = new ToggleComponent(this, ts.name(), f, o);
                    } else if (ss != null) {
                        tabComponent = new SelectorComponent(this, ss.name(), f, o, ss::items);
                    } else if (sliderSetting != null) {
                        tabComponent = new SliderComponent(this, sliderSetting.name(), f, o, sliderSetting.min(), sliderSetting.max(), sliderSetting.isInt(), sliderSetting.round());
                    }

                    if (ts == null && ss == null && sliderSetting == null) {
                        continue;
                    }

                    apply(tabComponent, o, items);

                    if (objectConsumer != null) {
                        for (Consumer<Object> consumer : objectConsumer) {
                            tabComponent.registerStateChange(consumer);
                        }
                    }
                }
            }
        }

        final List<CollapsibleTabComponent> c = new ArrayList<>(items.values());
        components.addAll(c);
    }

    public HashMap<Field, List<Consumer<Object>>> getCallbacks() {
        return callbacks;
    }

    public List<IOptionSetProvider> getSettingsObjects() {
        return settingsObjects;
    }

    @Override
    protected void pack() {
        show = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int yg = (height / 10); // Y grid
        int xg = (width / 11);  // X grid

        if (Minecraft.getMinecraft().theWorld == null) renderHyperiumBackground(ResolutionUtil.current());

        GlStateModifier.INSTANCE.reset();

        if (Minecraft.getMinecraft().theWorld == null) {
            this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
            this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        }
        drawRect(xg, yg, xg * 10, yg * 9, new Color(0, 0, 0, 225 / 2).getRGB());
        GlStateModifier.INSTANCE.reset();

        title.drawCenteredString("Settings", this.width / 2, yg + (yg / 2 - 8), 0xFFFFFF);

        renderCt(xg, yg * 2, xg * 9, yg * 7);
    }

    private void renderCt(int x, int y, int width, int height) {
        ScaledResolution sr = ResolutionUtil.current();
        int sw = sr.getScaledWidth();
        int sh = sr.getScaledHeight();
        int xg = width / 9;   // X grid

        /* Begin new scissor state */
        ScissorState.scissor(x, y, width, height, true);

        /* Get mouse X and Y */
        final int mx = Mouse.getX() * sw / Minecraft.getMinecraft().displayWidth;           // Mouse X
        final int my = sh - Mouse.getY() * sh / Minecraft.getMinecraft().displayHeight - 1; // Mouse Y

        if (scrollAnim.getValue() != scroll * 18 && scrollAnim.isFinished()) {
            scrollAnim = new SimpleAnimValue(1000L, scrollAnim.getValue(), scroll * 18);
        }
        y += scrollAnim.getValue();
        /* Render each tab component */
        for (AbstractTabComponent comp : components) {
            comp.render(x, y, width, mx, my);

            /* If mouse is over component, set as hovered */
            if (mx >= x && mx <= x + width && my > y && my <= y + comp.getHeight()) {
                comp.hover = true;
                // For slider
                comp.mouseEvent(mx - xg, my - y /* Make the Y relevant to the component */);
                if (Mouse.isButtonDown(0)) {
                    if (!clickStates.computeIfAbsent(comp, ignored -> false)) {
                        comp.onClick(mx, my - y /* Make the Y relevant to the component */);
                        clickStates.put(comp, true);
                    }
                } else if (clickStates.computeIfAbsent(comp, ignored -> false)) {
                    clickStates.put(comp, false);
                }
            } else {
                comp.hover = false;
            }
            y += comp.getHeight();
        }

        /* End scissor state */
        ScissorState.endScissor();

        GlStateManager.pushMatrix();
        GlStateManager.popMatrix();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void show() {
        // Set user's GUI scale to normal whilst the GUI is open.
        initialGuiScale = Minecraft.getMinecraft().gameSettings.guiScale;
        Minecraft.getMinecraft().gameSettings.guiScale = 2;
        super.show();
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

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Hyperium.CONFIG.save();
        Minecraft.getMinecraft().gameSettings.guiScale = initialGuiScale;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (Mouse.getEventDWheel() > 0) {
            scroll++;
        } else if (Mouse.getEventDWheel() < 0) {
            scroll--;
        }
        if (scroll > 0) {
            scroll = 0;
        }
    }

    private void apply(AbstractTabComponent component, IOptionSetProvider provider, HashMap<IOptionSetProvider, CollapsibleTabComponent> items) {
        CollapsibleTabComponent collapsibleTabComponent = items.computeIfAbsent(
            provider,
            providerObj -> new CollapsibleTabComponent(this, providerObj.getName())
        );
        collapsibleTabComponent.addChild(component);
    }
}
