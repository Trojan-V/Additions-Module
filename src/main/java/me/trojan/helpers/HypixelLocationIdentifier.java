package me.trojan.helpers;

import net.eq2online.util.Game;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HypixelLocationIdentifier {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static String getHypixelLocation() {
        String scoreboardLocation = getLocationFromScoreboard();
        String tablistLocation = getLocationFromTablist();
        // TODO: Scan blocks in Prototype Lobby, just like the bedrock in the priv island, but more blocks (array of blocks).
        if(scoreboardLocation.equals("SkyBlock Prototype Lobby"))
            return "SkyBlock Prototype Lobby";
        else if(scoreboardLocation.equals("SkyBlock Private Island") ||
                tablistLocation.equals("Private Island") || hasDefaultBedrock())
            return "SkyBlock Private Island";
            // TODO: Scan blocks in Prototype Lobby, just like the bedrock in the priv island, but more blocks (array of blocks).
        else if(scoreboardLocation.equals("SkyBlock Hub Island") || tablistLocation.equals("Hub"))
            return "SkyBlock Hub Island";
        return "Unknown Location";
    }

    private static String getLocationFromScoreboard() {
        String scoreboard = GuiUtils.Scoreboard.getScoreboard();
        for(HubLocation location : HubLocation.values()) {
            if(scoreboard.contains(location.toString()))
                return "SkyBlock Hub Island";
        }
        if(scoreboard.contains("Hype: "))
            return "SkyBlock Prototype Lobby";
        else if(scoreboard.contains("Your Island"))
            return "SkyBlock Private Island";
        return "Unknown Location";
    }

    private static String getLocationFromTablist() {
        for(NetworkPlayerInfo info : mc.player.connection.getPlayerInfoMap()) {
            String name = mc.ingameGUI.getTabList().getPlayerName(info);
            name = StringUtils.stripControlCodes(name);
            if(name.startsWith("Area: ")) {
                Pattern p = Pattern.compile("Area: (.+)");
                Matcher m = p.matcher(name);
                if(m.find())
                    return m.group(1);
            }
        }
        return "Unknown Location";
    }


    /* Additional identifier for private island. */
    private static boolean hasDefaultBedrock() {
        Block block = mc.world.getBlockState(new BlockPos(7, 97, 7)).getBlock();
        String blockName = Game.getBlockName(block);
        return blockName.equals("bedrock");
    }

    /* Additional identifier for hub island. */
    private boolean hasDefaultPortalToIsland() {
        String BLOCK_COMBO = "";

        StringBuilder sb = new StringBuilder();
        for(int x = -1; x > -5; x--) {
            Game.addChatMessage("Outer: " + x);
            for(int y = 70; y < 71; y++) {
                Game.addChatMessage("Inner: " + y);
                for(int z = -67; z < -66; z++) {
                    Game.addChatMessage("Most Inner: " + z);
                    Block block = mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                    String blockName = Game.getBlockName(block);
                    sb.append(blockName).append("|");
                }
            }
        }
        return sb.toString().equals(BLOCK_COMBO);
    }

    private enum HubLocation {
        AUCTION_HOUSE("Auction House"), BAZAAR_ALLEY("Bazaar Alley"), BLACKSMITH("Blacksmith"), CANVAS_ROOM("Canvas Room"), COAL_MINE("Coal Mine"),
        COLOSSEUM("Colosseum"), COLOSSEUM_ARENA("Colosseum Arena"), COMMUNITY_CENTER("Community Center"), ELECTION_ROOM("Election Room"),
        FARM("Farm"), FARMHOUSE("Farmhouse"), FASHION_SHOP("Fashion Shop"), FISHERMANS_HUT("Fisherman's Hut"),
        FLOWER_HOUSE("Flower House"), FOREST("Forest"), GRAVEYARD("Graveyard"), HEXATORUM("Hexatorum"),
        HIGH_LEVEL("High Level"), LIBRARY("Library"), MOUNTAIN("Mountain"), MUSEUM("Museum"),
        RUINS("Ruins"), TAVERN("Tavern"), THAUMATURGIST("Thaumaturgist"), VILLAGE("Village"),
        WILDERNESS("Wilderness");

        /*
         location is the value within the brackets
         VILLAGE("Village") -> "Village"
        */
        private final String location;

        HubLocation(String location) {
            this.location = location;
        }

        @Override
        public String toString() {
            return this.location;
        }
    }
}
