/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A configuration option.
 * This annotation ensures the value of the annotated field will be saved in the file,
 * assuming the parent class has been registered.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface ConfigOpt {
    /**
     * @deprecated This no longer does anything.
     * 
     * @return The default value, please don't override it.
     */
    @Deprecated
    String alt() default "";

    /**
     * The translation string for this option. If it doesn't have one, just don't override this.
     * The value of this will be passed to {@link net.minecraft.client.resources.I18n#format(String, Object...)}.
     * Note: this will be ignored if the game language is English.
     * 
     * @return The string to run a locale lookup for.
     */
    String i18n() default "";
}
