package cc.hyperium.utils;

/**
 * A simple animation value.
 */
public class SimpleAnimValue {
    private Long startMs;
    private Long duration;

    private float start;
    private float end;

    /**
     * Creates a new simple animation value with the specified data.
     *
     * @param duration The animation's duration in milliseconds.
     * @param start The animation's starting value.
     * @param end The animation's ending value.
     */
    public SimpleAnimValue(Long duration, float start, float end) {
        this.duration = duration;
        this.start = start;
        this.end = end;
        this.startMs = System.currentTimeMillis();
    }

    /**
     * Get the current progress of the animation.
     *
     * @return The animation progress.
     */
    public float getValue() {
        if (end - start == 0)
            return end;
        float v = start + ((float) (System.currentTimeMillis() - startMs)) * (((float) duration) / (end - start));
        return end > start ? Math.min(v, end) : Math.max(v, end);
    }

    /**
     * Get if the animation is finished.
     *
     * @return If the animation is finished.
     */
    public boolean isFinished() {
        return getValue() == end;
    }
}