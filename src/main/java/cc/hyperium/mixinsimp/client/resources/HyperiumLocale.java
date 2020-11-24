package cc.hyperium.mixinsimp.client.resources;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * Hyperium's i18n manager.
 */
public class HyperiumLocale {
    /**
     * Please don't modify this directly externally!
     */
    public static final Multimap<String, Supplier<InputStream>> LANG_FILES = ArrayListMultimap.create();

    /**
     * Registers a Hyperium language file, which adds extra translations to the Minecraft locale preset.
     * 
     * Note: this does not add unique languages, it simply adds extra strings to the Minecraft database,
     * meaning you can call {@link net.minecraft.client.resources.I18n#format(String, Object...)} with
     * either a Minecraft translation string, or a Hyperium translation string.
     * 
     * @param lang The language name.
     */
    public static void registerHyperiumLang(final String lang) {
        HyperiumLocale.LANG_FILES.put(
            lang,
            () -> HyperiumLocale.class.getResourceAsStream("/assets/hyperium/lang/" + lang + ".lang")
        );
    }
}
