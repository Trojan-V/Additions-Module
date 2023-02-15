package me.trojan.helpers;

import me.trojan.Cache;
import net.eq2online.util.Game;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/*
UTILS FOR:
- BossBar
- Scoreboard
- Tablist
 */
public class GuiUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    private GuiUtils() {}

    public static class BossBar {
        public static List<String> getBossBarText() {
            List<String> stringList = new ArrayList<>();
            try {
                if(Cache.mapBossInfo == null)
                    getAndCacheMapBossInfoField();

                Object mapBossInfoObject = Cache.mapBossInfo.get(mc.ingameGUI.getBossOverlay());
                @SuppressWarnings("unchecked")
                Map<UUID, BossInfoClient> mapBossInfoAsMap = (Map<UUID, BossInfoClient>) mapBossInfoObject;

                for (BossInfoClient bossBarInfo : mapBossInfoAsMap.values()) {
                    stringList.add(bossBarInfo.getName().getUnformattedText());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return stringList;
        }

        public static List<String> getBossBarPercentage() {
            List<String> floatsAsString = new ArrayList<>();
            try {
                if(Cache.mapBossInfo == null)
                    getAndCacheMapBossInfoField();

                Object mapBossInfoObject = Cache.mapBossInfo.get(mc.ingameGUI.getBossOverlay());
                @SuppressWarnings("unchecked")
                Map<UUID, BossInfoClient> mapBossInfoMap = (Map<UUID, BossInfoClient>) mapBossInfoObject;

                for (BossInfoClient bossBarInfo : mapBossInfoMap.values()) {
                    float bossBarPercentage = bossBarInfo.getPercent();
                    float actualPercentageValue = convertToActualPercentageValue(bossBarPercentage);
                    floatsAsString.add(String.valueOf(actualPercentageValue));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return floatsAsString;
        }

        public static List<String> getBossBarColor() {
            List<String> stringList = new ArrayList<>();
            try {
                if(Cache.mapBossInfo == null)
                    getAndCacheMapBossInfoField();

                Object mapBossInfoObject = Cache.mapBossInfo.get(mc.ingameGUI.getBossOverlay());
                @SuppressWarnings("unchecked")
                Map<UUID, BossInfoClient> mapBossInfoMap = (Map<UUID, BossInfoClient>) mapBossInfoObject;

                for (BossInfoClient bossBarInfo : mapBossInfoMap.values()) {
                    String colorAsString = bossBarInfo.getColor().toString();
                    stringList.add(colorAsString);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return stringList;
        }

        /* Obtaining this field once and then storing it within a Cache-class due to Reflection being power-intensive. */
        private static void getAndCacheMapBossInfoField() {
            // Name: g => field_184060_g => mapBossInfos
            // AT: public net.minecraft.client.gui.GuiBossOverlay field_184060_g # mapBossInfos
            String mapBossInfoFieldObf = "field_184060_g";
            try {
                Field mapBossInfo = ReflectionHelper.accessPrivateField(GuiBossOverlay.class, mapBossInfoFieldObf);
                ReflectionHelper.removeFinalModifier(mapBossInfo);
                Cache.mapBossInfo = mapBossInfo;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                Game.addChatMessage("Field: MapBossInfo (field_184060_g) is not available or the access attempt was illegal." +
                        " (Additions -> me.trojan.helpers.GuiUtils); Error Message: " + e.getMessage());
                e.printStackTrace();
            }
        }

        private static float convertToActualPercentageValue(float value) {
            return value * 100;
        }
    }

    public static class Tablist {
        /*
        Returns the "normal" part of the tablist
     Usually contains all players (and fake players if any have been injected by the server).
        */
        public static List<String> getTablist() {
            List<String> tablistEntries = new ArrayList<>();
            for(NetworkPlayerInfo info : mc.player.connection.getPlayerInfoMap()) {
                String name = mc.ingameGUI.getTabList().getPlayerName(info);
                tablistEntries.add(name);
            }
            return tablistEntries;
        }


        private static void getAndCacheTablistHeaderField() {
            String tablistHeaderFieldObf = "field_175256_i";
            try {
                Cache.tablistHeader = ReflectionHelper.accessPrivateField(GuiPlayerTabOverlay.class, tablistHeaderFieldObf);
            } catch (NoSuchFieldException e) {
                Game.addChatMessage("Field: TablistHeader (field_175256_i) is not available." +
                        " (Additions -> me.trojan.helpers.GuiUtils); Error Message: " + e.getMessage());
                e.printStackTrace();
            }
        }

        private static void getAndCacheTablistFooterField() {
            String tablistFooterFieldObf = "field_175255_h";
            try {
                Cache.tablistFooter = ReflectionHelper.accessPrivateField(GuiPlayerTabOverlay.class, tablistFooterFieldObf);
            } catch (NoSuchFieldException e) {
                Game.addChatMessage("Field: TablistFooter () is not available." +
                        " (Additions -> me.trojan.helpers.GuiUtils); Error Message: " + e.getMessage());
                e.printStackTrace();
            }
        }

        /*
        Returns the data the Tablist Header contains.
        This data can be modified by calling the following method:
        mc.ingameGUI.getTabList().setHeader
        */
        public static ITextComponent getHeaderComponent() {
            if(Cache.tablistHeader == null)
                getAndCacheTablistHeaderField();

            ITextComponent headerData = new TextComponentString("No header data found.");
            try {
                Object currInstance = Cache.tablistHeader.get(mc.ingameGUI.getTabList());
                headerData = (ITextComponent) currInstance;
            } catch (IllegalAccessException e) {
                Game.addChatMessage("Couldn't call 'get' on tablistHeader field for some reason (IllegalAccessException)." +
                        " (Additions -> me.trojan.helpers.GuiUtils); Error Message: " + e.getMessage());
                e.printStackTrace();
            }
            return headerData;
        }

        /*
        Returns the data the Tablist Footer contains.
        This data can be modified by calling the following method:
        mc.ingameGUI.getTabList().setFooter
        */
        public static ITextComponent getFooterComponent() {
            if(Cache.tablistFooter == null)
                getAndCacheTablistFooterField();

            ITextComponent footerData = new TextComponentString("No footer data found.");
            try {
                Object currInstance = Cache.tablistFooter.get(mc.ingameGUI.getTabList());
                footerData = (ITextComponent) currInstance;
            } catch (IllegalAccessException e) {
                Game.addChatMessage("Couldn't call 'get' on tablistFooter field for some reason (IllegalAccessException)." +
                        " (Additions -> me.trojan.helpers.GuiUtils); Error Message: " + e.getMessage());
                e.printStackTrace();
            }
            return footerData;
        }
    }


    public static class Scoreboard {
        public static String getScoreboard() {
            StringBuilder sb = new StringBuilder();
            for (ScorePlayerTeam team : mc.world.getScoreboard().getTeams()) {
                sb.append(team.getPrefix()).append(team.getSuffix());
            }
            return sb.toString();
        }
    }


    public static class Anvil {
        private static void getAndCacheAnvilTextField() {
            // Name: x => field_147091_w => nameField
            //AT: public net.minecraft.client.gui.GuiRepair field_147091_w # nameField
            String anvilTextFieldObf = "field_147091_w";
            try {
                Cache.anvilText = ReflectionHelper.accessPrivateField(GuiRepair.class, anvilTextFieldObf);
            } catch (NoSuchFieldException e) {
                Game.addChatMessage("Field: AnvilText (field_147091_w) is not available." +
                        " (Additions -> me.trojan.helpers.GuiUtils); Error Message: " + e.getMessage());
                e.printStackTrace();
            }
        }

        public static String getAnvilText() {
            String anvilText = "None";
            if(isNotInAnvilGui())
                return anvilText;

            if(Cache.anvilText == null)
                getAndCacheAnvilTextField();

            try {
                Object currInstance = Cache.anvilText.get(mc.currentScreen);
                GuiTextField nameField = (GuiTextField) currInstance;
                anvilText = nameField.getText();
            } catch (IllegalAccessException e) {
                Game.addChatMessage("Couldn't call 'get' on anvilText field for some reason (IllegalAccessException)." +
                        " (Additions -> me.trojan.helpers.GuiUtils); Error Message: " + e.getMessage());
                e.printStackTrace();
            }
            return anvilText;
        }

        public static void setAnvilText(String anvilText) {
            if(isNotInAnvilGui())
                return;

            if(Cache.anvilText == null)
                getAndCacheAnvilTextField();

            try {
                Object currInstance = Cache.anvilText.get(mc.currentScreen);
                GuiTextField textField = (GuiTextField) currInstance;
                textField.setText(anvilText);

                ContainerRepair container = (ContainerRepair) mc.player.openContainer;
                container.updateItemName(anvilText);
                container.updateRepairOutput();
            } catch (IllegalAccessException e) {
                Game.addChatMessage("Couldn't call 'get' on anvilText field for some reason (IllegalAccessException)." +
                        " (Additions -> me.trojan.helpers.GuiUtils); Error Message: " + e.getMessage());
                e.printStackTrace();
            }
        }

        private static boolean isNotInAnvilGui() {
            return !(mc.currentScreen instanceof GuiRepair);
        }
    }

}
