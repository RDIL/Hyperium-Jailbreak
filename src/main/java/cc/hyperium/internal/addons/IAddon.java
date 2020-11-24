package cc.hyperium.internal.addons;

/**
 * All external addons must have a "main class" which implements this interface.
 */
public interface IAddon {
    /**
     * Called when the addon is loaded. Do any setup here.
     */
    void onLoad();

    /**
     * Called when the game (but first, this addon) is shutting down. Do any clean-up/last minute shutdown here.
     */
    void onClose();

    /**
     * @deprecated This no longer does anything and should not be used.
     */
    @Deprecated default void sendDebugInfo() {}
}
