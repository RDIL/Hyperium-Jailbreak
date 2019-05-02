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

package cc.hyperium.handlers.handlers;

import cc.hyperium.config.ConfigOpt;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RankedRatingChangeEvent;

public class ValueHandler {
    @ConfigOpt
    private int rankedRating;
    @ConfigOpt
    private int deltaRankedRating;

    @InvokeEvent
    public void rankedRatingChange(RankedRatingChangeEvent event) {
        this.rankedRating = event.getRating();
        this.deltaRankedRating = event.getDelta();
    }

    public int getDeltaRankedRating() {
        return deltaRankedRating;
    }

    public int getRankedRating() {
        return rankedRating;
    }
}