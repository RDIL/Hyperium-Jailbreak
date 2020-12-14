package cc.hyperium.gui.integrations;

import net.hypixel.api.HypixelApiFriendObject;

import java.util.Comparator;

enum FriendSortType implements Comparator<HypixelApiFriendObject> {
    ALPHABETICAL("Alphabetical") {
        @Override
        public int compare(HypixelApiFriendObject o1, HypixelApiFriendObject o2) {
            return o1.getName().compareTo(o2.getName());
        }
    },

    RANK("Rank") {
        @Override
        public int compare(HypixelApiFriendObject o1, HypixelApiFriendObject o2) {
            int compare = Integer.compare(o1.rankOrdinal(), o2.rankOrdinal());
            if (compare == 0) {
                return o1.getName().compareTo(o2.getName());
            }
            return compare;
        }
    },

    DATE_ADDED("Date Added") {
        @Override
        public int compare(HypixelApiFriendObject o1, HypixelApiFriendObject o2) {
            return Long.compare(o1.getAddedOn(), o2.getAddedOn());
        }
    },

    LOGOFF("Latest Logoff") {
        @Override
        public int compare(HypixelApiFriendObject o1, HypixelApiFriendObject o2) {
            // Reverse it so most recent is first.
            return Long.compare(o1.getLogoff(), o2.getLogoff()) * -1;
        }
    },

    NONE("NONE") {
        @Override
        public int compare(HypixelApiFriendObject o1, HypixelApiFriendObject o2) {
            return 0;
        }
    };

    String name;

    FriendSortType(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}
