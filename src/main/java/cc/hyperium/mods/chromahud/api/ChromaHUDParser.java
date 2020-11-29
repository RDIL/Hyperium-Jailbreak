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

package cc.hyperium.mods.chromahud.api;

import cc.hyperium.utils.JsonHolder;
import java.util.Map;

/**
 * The base interface for a ChromaHUD display item parser.
 */
public interface ChromaHUDParser {
    /**
     * Parse a display item from its data.
     *
     * @param type The type ID of the element.
     * @param ord The ordinal of the item.
     * @param item The JSON data of the item.
     * @return A {@link DisplayItem} object.
     */
    DisplayItem parse(String type, int ord, JsonHolder item);

    /**
     * Get the names of the items registered.
     * The scheme is ID to Display Name.
     *
     * @return The ID to Display Name {@link Map}.
     */
    Map<String, String> getNames();
}
