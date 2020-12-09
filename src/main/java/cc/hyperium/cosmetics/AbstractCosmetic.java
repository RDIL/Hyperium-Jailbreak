package cc.hyperium.cosmetics;

/**
 * An abstract cosmetic item.
 */
public abstract class AbstractCosmetic {
    public AbstractCosmetic() { }

    /**
     * Interpolates yaw1 and yaw2 based on the percent.
     *
     * @param yaw1 The first yaw value.
     * @param yaw2 The second yaw value.
     * @param percent The percent.
     * @return The calculated number.
     */
    public static float interpolate(final float yaw1, final float yaw2, final float percent) {
        float f = (yaw1 + (yaw2 - yaw1) * percent) % 360.0f;
        if (f < 0.0f)  f += 360.0f;
        return f;
    }
}
