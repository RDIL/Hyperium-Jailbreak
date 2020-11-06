package cc.hyperium.internal.addons;

/**
 * The interface that external addons implement.
 */
public interface IAddon {
    /**
     * Called when the addon is loaded.
     */
    void onLoad();

    /**
     * Called when the game is being shut down.
     */
    void onClose();

    /**
     * @deprecated This field has never been used, and can just be left alone.
     */
    @Deprecated default void sendDebugInfo() {
    }
}
