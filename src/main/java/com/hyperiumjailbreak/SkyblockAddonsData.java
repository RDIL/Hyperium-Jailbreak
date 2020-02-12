package com.hyperiumjailbreak;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import cc.hyperium.Hyperium;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public class SkyblockAddonsData {
    public enum Location {
        ISLAND("Your Island"),
        VILLAGE("Village"),
        AUCTION_HOUSE("Auction House"),
        BANK("Bank"),
        LIBRARY("Library"),
        COAL_MINE("Coal Mine"),
        GRAVEYARD("Graveyard"),
        COLOSSEUM("Colosseum"),
        WILDERNESS("Wilderness"),
        MOUNTAIN("Mountain"),
        WIZARD_TOWER("Wizard Tower"),
        RUINS("Ruins"),
        FOREST("Forest"),
        FARM("Farm"),
        FISHERMANS_HUT("Fisherman's Hut"),
        HIGH_LEVEL("High Level"),
        FLOWER_HOUSE("Flower House"),
        CANVAS_ROOM("Canvas Room"),
        TAVERN("Tavern"),
        BIRCH_PARK("Birch Park"),
        SPRUCE_WOODS("Spruce Woods"),
        JUNGLE_ISLAND("Jungle Island"),
        SAVANNA_WOODLAND("Savanna Woodland"),
        DARK_THICKET("Dark Thicket"),
        GOLD_MINE("Gold Mine"),
        DEEP_CAVERNS("Deep Caverns"),
        GUNPOWDER_MINES("Gunpowder Mines"),
        LAPIS_QUARRY("Lapis Quarry"),
        PIGMAN_DEN("Pigmen's Den"),
        SLIMEHILL("Slimehill"),
        DIAMOND_RESERVE("Diamond Reserve"),
        OBSIDIAN_SANCTUARY("Obsidian Sanctuary"),
        THE_BARN("The Barn"),
        MUSHROOM_DESERT("Mushroom Desert"),
        SPIDERS_DEN("Spider's Den"),
        BLAZING_FORTRESS("Blazing Fortress"),
        THE_END("The End"),
        DRAGONS_NEST("Dragon's Nest");

        private String scoreboardName;

        Location(String scoreboardName) {
            this.scoreboardName = scoreboardName;
        }

        public String getScoreboardName() {
            return scoreboardName;
        }
    }

    public enum SkyblockNPC {
        AUCTION_MASTER(17.5,71,-78.5, false, Location.VILLAGE, Location.AUCTION_HOUSE),
        BANKER(20.5,71,-40.5, false, Location.VILLAGE, Location.BANK),
        BAKER(34.5, 71, -44.5, false, Location.VILLAGE),
        LOBBY_SELECTOR(-9,70,-79, false, Location.VILLAGE),
        LUMBER_MERCHANT(-18.5,70,-90, true, Location.VILLAGE),
        ADVENTURER(-18.5,70,-77, true, Location.VILLAGE),
        FISH_MERCHANT(-25.5,70,-77, true, Location.VILLAGE),
        ARMORSMITH(-25.5,70,-90, true, Location.VILLAGE),
        BLACKSMITH(-19.5,71,-124.5, false, Location.VILLAGE),
        BLACKSMITH_2(-39.5,77,-299.5, false, Location.GOLD_MINE),
        FARM_MERCHANT(-7,70,-48.5, true, Location.VILLAGE),
        MINE_MERCHANT(-19,70,-48.5, true, Location.VILLAGE),
        WEAPONSMITH(-19,70,-41.5, false, Location.VILLAGE),
        BUILDER(-7,70,-41.5, true, Location.VILLAGE),
        LIBRARIAN(17.5,71,-16.5, true, Location.VILLAGE, Location.LIBRARY),
        MARCO(9.5,71,-14, false, Location.VILLAGE, Location.FLOWER_HOUSE),
        ALCHEMIST(-33.5,73,-14.5, true, Location.VILLAGE),
        PAT(-129.5,73,-98.5, true, Location.GRAVEYARD),
        EVENT_MASTER(-61.5,71,-54.5, false, Location.COLOSSEUM, Location.VILLAGE),
        GOLD_FORGER(-27.5,74,-294.5, true, Location.GOLD_MINE),
        IRON_FORGER(-1.5,75,-307.5, false, Location.GOLD_MINE),
        RUSTY(-20,78,-326, false, Location.GOLD_MINE),
        MADDOX_THE_SLAYER(-87,66,-70, false, Location.VILLAGE, Location.TAVERN),
        SIRIUS(91.5,75,176.5, false, Location.WILDERNESS);

        private final AxisAlignedBB hideArea;
        private double x;
        private double y;
        private double z;
        private boolean isMerchant;
        Set<Location> locations;

        SkyblockNPC(double x, double y, double z, boolean isMerchant, Location... locations) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.isMerchant = isMerchant;
            int hideRadius = 4;
            this.hideArea = new AxisAlignedBB(x - hideRadius, y - hideRadius, z - hideRadius, x + hideRadius, y + hideRadius, z + hideRadius);
            this.locations = EnumSet.copyOf(Arrays.asList(locations));
        }

        public boolean isAtLocation(Location location) {
            return locations.contains(location);
        }

        public boolean isNearEntity(Entity entity) {
            // Utils utils = SkyblockAddons.getInstance().getUtils();
            if (isAtLocation(Hyperium.INSTANCE.getUtils().getLocation())) {
                double x = entity.posX;
                double y = entity.posY;
                double z = entity.posZ;

                return this.hideArea.isVecInside(new Vec3(x, y, z)) && (this.x != x || this.y != y || this.z != z) && Hyperium.INSTANCE.getUtils().isNotNPC(entity);
            }
            return false;
        }

        public static boolean isNearNPC(Entity entity) {
            for (SkyblockNPC npc : values()) {
                if (npc.isNearEntity(entity)) {
                    return true;
                }
            }
            return false;
        }

        public static boolean isMerchant(String name) {// inventory
            for (SkyblockNPC npc : values()) {
                if (npc.isMerchant) {
                    if (name.replaceAll(" ", "_").equalsIgnoreCase(npc.name())) {
                        return true;
                    }
                }
            }
            return name.contains("Merchant");
        }
    }
}
