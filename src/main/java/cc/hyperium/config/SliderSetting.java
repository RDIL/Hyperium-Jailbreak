package cc.hyperium.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SliderSetting {
    float min();
    float max();
    String name();
    boolean round() default true;
    Category category() default Category.GENERAL;
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
    boolean isInt() default false;
}
