package cc.hyperium.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A setting that should be represented by a slider in the settings menu.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface SliderSetting {
    /**
     * The minimum value for the slider.
     * 
     * @return The minimum value.
     */
    float min();

    /**
     * The maximum value for the slider.
     * 
     * @return The maximum value.
     */
    float max();

    /**
     * The name of the option (in English).
     * 
     * @return The name.
     * @see ConfigOpt#i18n()
     */
    String name();

    /**
     * If the value of the option should be rounded to the nearest unit.
     * 
     * @return If the value of the option should be rounded.
     */
    boolean round() default true;

    /**
     * The category this option should be under.
     * 
     * @return The category.
     */
    Category category() default Category.GENERAL;

    /**
     * @deprecated This is deprecated and no longer does anything.
     * 
     * @return The default value, please don't override it.
     */
    @Deprecated
    boolean enabled() default true;

    /**
     * @deprecated This is deprecated and no longer does anything.
     * 
     * @return The default value, please don't override it.
     */
    @Deprecated
    boolean mods() default false;

    /**
     * If the option is an integer instead of a float.
     * 
     * @return If the value should be in integer format.
     */
    boolean isInt() default false;
}
