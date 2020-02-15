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

package cc.hyperium.mods;

import org.apache.commons.lang3.StringUtils;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@SuppressWarnings("unused")
public abstract class AbstractMod {
    public abstract AbstractMod init();

    public abstract Metadata getModMetadata();

    public static class Metadata {
        private final AbstractMod mod;
        private final String name;
        private final String version;

        private String displayName;

        public Metadata(AbstractMod mod, String name) {
            this(mod, name, "1");
        }

        public Metadata(AbstractMod mod, String name, String version) {
            this(mod, name, version, "");
        }

        public Metadata(AbstractMod mod, String name, String version, String author) {
            checkNotNull(mod, "Mod instance cannot be null");
            checkArgument(!StringUtils.isEmpty(name), "Name cannot be null or empty (" + name + ")");
            checkArgument(!StringUtils.isEmpty(version), "Version cannot be null or empty (" + version + ")");
            this.mod = mod;
            this.name = name;
            this.version = version;
            this.displayName = name;
        }

        public AbstractMod getMod() {
            return this.mod;
        }

        public String getName() {
            return this.name != null ? this.name : "";
        }

        public String getVersion() {
            return this.version;
        }

        @Deprecated
        public String getDisplayName() {
            return this.displayName != null ? this.displayName : getName();
        }
    }
}
