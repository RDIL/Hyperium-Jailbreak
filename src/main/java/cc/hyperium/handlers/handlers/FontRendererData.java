package cc.hyperium.handlers.handlers;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that just holds the {@link FontRendererData#stringWidthCache} values.
 *
 * @see FontRendererData#stringWidthCache
 */
public class FontRendererData {
    /**
     * The publicly usable instance of the class.
     */
    public static final FontRendererData INSTANCE = new FontRendererData();

    /**
     * This allows for the caching of the widths of different strings, so it doesn't need to be constantly recalculated.
     * This saves processing power, at the cost of using a little bit extra memory.
     */
    public final Map<String, Integer> stringWidthCache = new HashMap<>();

    private FontRendererData() {}
}
