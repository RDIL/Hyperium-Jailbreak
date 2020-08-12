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

package cc.hyperium.mixins;

import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(Scoreboard.class)
public abstract class MixinScoreboard {
    @Shadow @Final private Map<String, ScorePlayerTeam> teams;
    @Shadow @Final private Map<String, ScorePlayerTeam> teamMemberships;
    @Shadow public abstract ScorePlayerTeam getTeam(String p_96508_1_);
    @Shadow public abstract void func_96513_c(ScorePlayerTeam team);

    /**
     * @author hyperium
     */
    @Overwrite
    public void removeTeam(ScorePlayerTeam team) {
        if (team == null) {
            return;
        }

        if (team.getRegisteredName() != null) {
            teams.remove(team.getRegisteredName());
        }
        for (String s : team.getMembershipCollection()) {
            teamMemberships.remove(s);
        }

        this.func_96513_c(team);
    }

    /**
     * Instead of throwing an exception if the team requested to be created already exists, just return it.
     *
     * @author Reece Dunham
     */
    @Inject(at = @At("HEAD"), method = "createTeam", cancellable = true)
    public void createTeam(String name, CallbackInfoReturnable<ScorePlayerTeam> cir) {
        try {
            ScorePlayerTeam s = this.getTeam(name);
            if (s != null) {
                cir.setReturnValue(s);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // Ignore the exception and continue normally because it will just create the team like it was going to initially.
        }
    }
}
