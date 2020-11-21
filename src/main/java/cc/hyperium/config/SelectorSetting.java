package cc.hyperium.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface SelectorSetting {
    String name();
    Category category() default Category.GENERAL;
    String[] items();
    /**
     * @deprecated This field no longer does anything.
     */
    @Deprecated
    boolean enabled() default true;
    /**
     * @deprecated This field no longer does anything.
     */
    @Deprecated
    boolean mods() default false;
}
