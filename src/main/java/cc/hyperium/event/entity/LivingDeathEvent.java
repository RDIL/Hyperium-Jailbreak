/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.event.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public class LivingDeathEvent {
    private final EntityLivingBase entity;
    private final DamageSource cause;

    public LivingDeathEvent(EntityLivingBase entity, DamageSource cause) {
        this.entity = entity;
        this.cause = cause;
    }

    public EntityLivingBase getEntity() {
        return entity;
    }

    public DamageSource getCause() {
        return cause;
    }
}
