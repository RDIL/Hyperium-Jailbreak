package cc.hyperium.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A setting that should be represented by a multi-option selector in the settings menu.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface SelectorSetting {
    /**
     * Get the name of the option or the i18n key.
     * 
     * @return The name.
     */
    String name();

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
     * The items this selector can have as the selected value.
     * 
     * @return The items.
     */
    String[] items();
}
