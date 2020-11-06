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

package cc.hyperium.addons;

/**
 * Base class for internal addons.
 *
 * @deprecated For external addons, use {@link cc.hyperium.internal.addons.IAddon}, and migrate the rest to the internal mod system.
 */
@Deprecated
public abstract class AbstractAddon {
    /**
     * Called on initialization of the internal addon.
     *
     * @return The instance.
     * @deprecated For external addons, use {@link cc.hyperium.internal.addons.IAddon}, and migrate the rest to the internal mod system.
     */
    @Deprecated
    public abstract AbstractAddon init();

    /**
     * Get the metadata.
     *
     * @return The metadata.
     * @deprecated For external addons, use {@link cc.hyperium.internal.addons.IAddon}, and migrate the rest to the internal mod system.
     */
    @Deprecated
    public abstract Metadata getAddonMetadata();

    /**
     * A metadata wrapper.
     *
     * @deprecated For external addons, use {@link cc.hyperium.internal.addons.IAddon}, and migrate the rest to the internal mod system.
     */
    @Deprecated
    public static class Metadata {
        @Deprecated
        private final AbstractAddon addon;
        @Deprecated
        private final String name;
        @Deprecated
        private final String version;

        /**
         * @deprecated For external addons, use {@link cc.hyperium.internal.addons.IAddon}, and migrate the rest to the internal mod system.
         */
        @Deprecated
        public Metadata(AbstractAddon addon, String name) {
            this(addon, name, "1.0");
        }

        /**
         * @deprecated For external addons, use {@link cc.hyperium.internal.addons.IAddon}, and migrate the rest to the internal mod system.
         */
        @Deprecated
        public Metadata(AbstractAddon addon, String name, String version) {
            this(addon, name, version, "");
        }

        /**
         * @deprecated For external addons, use {@link cc.hyperium.internal.addons.IAddon}, and migrate the rest to the internal mod system.
         */
        @Deprecated
        public Metadata(AbstractAddon addon, String name, String version, String author) {
            this.addon = addon;
            this.name = name;
            this.version = version;
        }

        /**
         * @deprecated For external addons, use {@link cc.hyperium.internal.addons.IAddon}, and migrate the rest to the internal mod system.
         */
        @Deprecated
        public AbstractAddon getAddon() {
            return this.addon;
        }

        /**
         * @deprecated For external addons, use {@link cc.hyperium.internal.addons.IAddon}, and migrate the rest to the internal mod system.
         * @return The name.
         */
        @Deprecated
        public String getName() {
            return this.name;
        }

        /**
         * @deprecated For external addons, use {@link cc.hyperium.internal.addons.IAddon}, and migrate the rest to the internal mod system.
         */
        @Deprecated
        public String getVersion() {
            return this.version;
        }
    }
}
