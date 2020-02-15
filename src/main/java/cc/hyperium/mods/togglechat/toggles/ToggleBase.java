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

package cc.hyperium.mods.togglechat.toggles;

import cc.hyperium.utils.ChatColor;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.regex.Pattern;

public abstract class ToggleBase {
    public ToggleBase() {}

    public abstract String getName();

    public abstract boolean shouldToggle(final String message);

    public abstract boolean isEnabled();

    public abstract void setEnabled(boolean enabled);

    public void toggle() {
        setEnabled(!isEnabled());
    }

    public abstract LinkedList<String> getDescription();

    public final boolean hasDescription() {
        return getDescription() != null && !getDescription().isEmpty();
    }

    public String getDisplayName() {
        return getName() + ": %s";
    }

    public boolean useFormattedMessage() {
        return false;
    }

    @SafeVarargs
    public final <T> LinkedList<T> asLinked(T... entry) {
        return new LinkedList<>(Arrays.asList(entry));
    }

    public final boolean containsIgnoreCase(String message, String contains) {
        return Pattern.compile(Pattern.quote(contains), Pattern.CASE_INSENSITIVE).matcher(message).find();
    }

    public String getStatus(boolean in) {
        return in ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled";
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ToggleBase && getName().equals(((ToggleBase) other).getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
