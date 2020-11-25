package cc.hyperium.gui.hyperium.components;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.bossbar.gui.GuiBossbarSetting;
import cc.hyperium.addons.customcrosshair.gui.GuiCustomCrosshairEditCrosshair;
import cc.hyperium.addons.sidebar.gui.screen.GuiScreenSettings;
import cc.hyperium.config.Category;
import cc.hyperium.config.ConfigOpt;
import cc.hyperium.config.SelectorSetting;
import cc.hyperium.config.SliderSetting;
import cc.hyperium.config.ToggleSetting;
import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.gui.hyperium.RGBFieldSet;
import cc.hyperium.gui.keybinds.GuiKeybinds;
import cc.hyperium.mods.chromahud.gui.GeneralConfigGui;
import cc.hyperium.mods.keystrokes.screen.GuiScreenKeystrokes;
import cc.hyperium.mods.togglechat.gui.ToggleChatMainGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.common.base.Strings;

public class SettingsTab extends AbstractTab {
    public SettingsTab(HyperiumMainGui gui) {
        super(gui, "Settings");

        HashMap<Category, CollapsibleTabComponent> items = new HashMap<>();
        for (Object o : gui.getSettingsObjects()) {
            for (Field f : o.getClass().getDeclaredFields()) {
                final ConfigOpt co = f.getAnnotation(ConfigOpt.class);
                final ToggleSetting ts = f.getAnnotation(ToggleSetting.class);
                final SelectorSetting ss = f.getAnnotation(SelectorSetting.class);
                final SliderSetting sliderSetting = f.getAnnotation(SliderSetting.class);
                List<Consumer<Object>> objectConsumer = gui.getCallbacks().get(f);
                AbstractTabComponent tabComponent = null;
                Category category = null;
                if (ts != null) {
                    tabComponent = new ToggleComponent(this, Collections.emptyList(),
                        getLocalizedStringForOption(co, ts),
                        f, o);
                    category = ts.category();
                } else if (ss != null) {
                    Supplier<String[]> supplier = gui.getCustomStates().getOrDefault(f, ss::items);
                    tabComponent = new SelectorComponent(this, Collections.emptyList(),
                        getLocalizedStringForOption(co, ss),
                        f, o, supplier);
                    category = ss.category();
                } else if (sliderSetting != null) {
                    tabComponent = new SliderComponent(this, Collections.emptyList(),
                        getLocalizedStringForOption(co, sliderSetting),
                        f, o, sliderSetting.min(), sliderSetting.max(), sliderSetting.isInt(), sliderSetting.round());
                    category = sliderSetting.category();
                }
                if (category == null) continue;
                apply(tabComponent, category, items);
                if (objectConsumer != null) {
                    for (Consumer<Object> consumer : objectConsumer) {
                        tabComponent.registerStateChange(consumer);
                    }
                }
            }
        }

        apply(new LinkComponent(this, Collections.emptyList(), "Open Sidebar Customizer", new GuiScreenSettings(Hyperium.INSTANCE.getInternalAddons().getSidebarAddon())), Category.MODS, items);
        apply(new LinkComponent(this, Collections.emptyList(), "Open Keystrokes Customizer", new GuiScreenKeystrokes(Hyperium.INSTANCE.getModIntegration().getKeystrokesMod())), Category.KEYSTROKES, items);
        apply(new LinkComponent(this, Collections.emptyList(), "Open ToggleChat Customizer", new ToggleChatMainGui(Hyperium.INSTANCE.getModIntegration().getToggleChat(), 0)), Category.MODS, items);
        apply(new LinkComponent(this, Collections.emptyList(), "Open Keybind Customizer", new GuiKeybinds()), Category.GENERAL, items);
        apply(new LinkComponent(this, Collections.emptyList(), "Open Bossbar Customizer", new GuiBossbarSetting(Hyperium.INSTANCE.getInternalAddons().getBossbarAddon())), Category.MODS, items);
        apply(new LinkComponent(this, Collections.emptyList(), "Open Crosshair Customizer", new GuiCustomCrosshairEditCrosshair(Hyperium.INSTANCE.getInternalAddons().getCustomCrosshairAddon())), Category.INTEGRATIONS, items);
        apply(new LinkComponent(this, Collections.emptyList(), "Open ChromaHUD Customizer", new GeneralConfigGui(Hyperium.INSTANCE.getModIntegration().getChromaHUD())), Category.CHROMAHUD, items);

        for (RGBFieldSet rgbFieldSet : gui.getRgbFields()) {
            apply(new RGBComponent(this, rgbFieldSet), rgbFieldSet.getCategory(), items);
        }

        final Collection<CollapsibleTabComponent> values = items.values();
        List<CollapsibleTabComponent> c = new ArrayList<>(values);
        c.sort(Comparator.comparing(CollapsibleTabComponent::getLabel));
        components.addAll(c);
    }

    private void apply(AbstractTabComponent component, Category category, HashMap<Category, CollapsibleTabComponent> items) {
        CollapsibleTabComponent collapsibleTabComponent = items.computeIfAbsent(
            category,
            category1 ->
                new CollapsibleTabComponent(this, Collections.singletonList(category1.name()), category1.getDisplay())
        );
        collapsibleTabComponent.addChild(component);
    }

    public static String getLocalizedStringForOption(ConfigOpt configOpt, SelectorSetting ss) {
        return getLocalizedStringForOption(configOpt, ss.name());
    }

    public static String getLocalizedStringForOption(ConfigOpt configOpt, ToggleSetting ts) {
        return getLocalizedStringForOption(configOpt, ts.name());
    }

    public static String getLocalizedStringForOption(ConfigOpt configOpt, SliderSetting ss) {
        return getLocalizedStringForOption(configOpt, ss.name());
    }

    public static String getLocalizedStringForOption(ConfigOpt configOpt, String name) {
        final boolean isEnglish = Minecraft.getMinecraft().gameSettings.language.equals("en_US");

        // game is in English, no need to translate
        if (isEnglish) {
            return name;
        }

        // has no i18n string
        if (Strings.isNullOrEmpty(configOpt.i18n())) {
            return name;
        }

        final String formatted = I18n.format(configOpt.i18n());

        // formatting failed, fallback to English
        if (formatted.contains(".")) {
            return name;
        }

        return formatted;
    }
}
