package cc.hyperium.gui.hyperium.tabs;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.bossbar.gui.GuiBossbarSetting;
import cc.hyperium.addons.customcrosshair.gui.GuiCustomCrosshairEditCrosshair;
import cc.hyperium.addons.sidebar.gui.screen.GuiScreenSettings;
import cc.hyperium.config.Category;
import cc.hyperium.config.SelectorSetting;
import cc.hyperium.config.SliderSetting;
import cc.hyperium.config.ToggleSetting;
import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.gui.hyperium.RGBFieldSet;
import cc.hyperium.gui.hyperium.components.AbstractTab;
import cc.hyperium.gui.hyperium.components.LinkComponent;
import cc.hyperium.gui.hyperium.components.AbstractTabComponent;
import cc.hyperium.gui.hyperium.components.CollapsibleTabComponent;
import cc.hyperium.gui.hyperium.components.RGBComponent;
import cc.hyperium.gui.hyperium.components.SelectorComponent;
import cc.hyperium.gui.hyperium.components.SliderComponent;
import cc.hyperium.gui.hyperium.components.ToggleComponent;
import cc.hyperium.gui.keybinds.GuiKeybinds;
import cc.hyperium.mods.chromahud.gui.GeneralConfigGui;
import cc.hyperium.mods.keystrokes.screen.GuiScreenKeystrokes;
import cc.hyperium.mods.togglechat.gui.ToggleChatMainGui;
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

public class SettingsTab extends AbstractTab {
    public SettingsTab(HyperiumMainGui gui) {
        super(gui, "tab.settings.name");

        HashMap<Category, CollapsibleTabComponent> items = new HashMap<>();
        for (Object o : gui.getSettingsObjects()) {
            for (Field f : o.getClass().getDeclaredFields()) {
                ToggleSetting ts = f.getAnnotation(ToggleSetting.class);
                SelectorSetting ss = f.getAnnotation(SelectorSetting.class);
                SliderSetting sliderSetting = f.getAnnotation(SliderSetting.class);
                List<Consumer<Object>> objectConsumer = gui.getCallbacks().get(f);
                AbstractTabComponent tabComponent = null;
                Category category = null;
                boolean mods = false;
                if (ts != null) {
                    tabComponent = new ToggleComponent(this, Collections.emptyList(), I18n.format(ts.name()), f, o);
                    category = ts.category();
                    mods = ts.mods();
                } else if (ss != null) {
                    Supplier<String[]> supplier = gui.getCustomStates().getOrDefault(f, ss::items);
                    tabComponent = new SelectorComponent(this, Collections.emptyList(), I18n.format(ss.name()), f, o, supplier);
                    category = ss.category();
                    mods = ss.mods();
                } else if (sliderSetting != null) {
                    tabComponent = new SliderComponent(this, Collections.emptyList(), I18n.format(sliderSetting.name()), f, o, sliderSetting.min(), sliderSetting.max(), sliderSetting.isInt(), sliderSetting.round());
                    category = sliderSetting.category();
                    mods = sliderSetting.mods();
                }
                if (category == null) continue;
                apply(tabComponent, mods, category, items);
                if (objectConsumer != null) {
                    for (Consumer<Object> consumer : objectConsumer) {
                        tabComponent.registerStateChange(consumer);
                    }
                }
            }
        }

        apply(new LinkComponent(this, Collections.emptyList(), "Sidebar Mod", new GuiScreenSettings(Hyperium.INSTANCE.getInternalAddons().getSidebarAddon())), true, Category.MODS, items);
        apply(new LinkComponent(this, Collections.emptyList(), "Keystrokes Config GUI", new GuiScreenKeystrokes(Hyperium.INSTANCE.getModIntegration().getKeystrokesMod())), true, Category.KEYSTROKES, items);
        apply(new LinkComponent(this, Collections.emptyList(), "Togglechat", new ToggleChatMainGui(Hyperium.INSTANCE.getModIntegration().getToggleChat(), 0)), true, Category.MODS, items);
        apply(new LinkComponent(this, Collections.emptyList(), "Keybinds", new GuiKeybinds()), false, Category.GENERAL, items);
        apply(new LinkComponent(this, Collections.emptyList(), "Bossbar Customizer", new GuiBossbarSetting()), true, Category.MODS, items);
        apply(new LinkComponent(this, Collections.emptyList(), "Custom Crosshair", new GuiCustomCrosshairEditCrosshair(Hyperium.INSTANCE.getInternalAddons().getCustomCrosshairAddon())), true, Category.INTEGRATIONS, items);
        apply(new LinkComponent(this, Collections.emptyList(), "ChromaHUD", new GeneralConfigGui(Hyperium.INSTANCE.getModIntegration().getChromaHUD())), true, Category.CHROMAHUD, items);

        for (RGBFieldSet rgbFieldSet : gui.getRgbFields()) {
            apply(new RGBComponent(this, rgbFieldSet), rgbFieldSet.isMods(), rgbFieldSet.getCategory(), items);
        }

        Collection<CollapsibleTabComponent> values = items.values();
        List<CollapsibleTabComponent> c = new ArrayList<>(values);
        for (CollapsibleTabComponent value : values) {
            value.sortSelf();
        }
        c.sort(Comparator.comparing(CollapsibleTabComponent::getLabel));
        components.addAll(c);
    }

    private void apply(AbstractTabComponent component, boolean mods, Category category, HashMap<Category, CollapsibleTabComponent> items) {
        CollapsibleTabComponent collapsibleTabComponent = items.computeIfAbsent(
            (mods ? Category.MODS : category),
            category1 ->
                new CollapsibleTabComponent(SettingsTab.this,
                    Collections.singletonList(category1.name()),
                    category1.getDisplay()));
        if (mods) {
            boolean b = false;
            for (AbstractTabComponent abs : collapsibleTabComponent.getChildren()) {
                if (((CollapsibleTabComponent) abs).getLabel().equalsIgnoreCase(category.getDisplay())) {
                    ((CollapsibleTabComponent) abs).addChild(component);
                    b = true;
                }
            }
            if (!b) {
                CollapsibleTabComponent c = new CollapsibleTabComponent(this, Collections.emptyList(), category.getDisplay());
                collapsibleTabComponent.addChild(c);
                c.addChild(component);
                c.setParent(collapsibleTabComponent);
            }
        } else {
            collapsibleTabComponent.addChild(component);
        }
    }
}
