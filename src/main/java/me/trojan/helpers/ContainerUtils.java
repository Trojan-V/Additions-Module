package me.trojan.helpers;

import net.eq2online.macros.core.mixin.IGuiContainer;
import net.eq2online.util.Game;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;

import java.util.List;


/*
UTILS FOR EVERYTHING THAT HAS TO DO WITH CONTAINERS.
 */
public class ContainerUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    /**
     * private constructor, no instances.
     */
    private ContainerUtils() {}

    public static boolean checkIfInvIsFull() {
        boolean invIsFull = true;
        List<Slot> inventoryContent = mc.player.inventoryContainer.inventorySlots;
        /*
         Slot 9 is where the actual inventory starts, the other slots are the Crafting Grid and the Armor Inventory.
         44 is the last slot of the inventory, when no other GUI is open.
        */
        for(int i = 9; i < 45; i++) {
            if(Game.getItemName(inventoryContent.get(i).getStack().getItem()).equals("air")) {
                invIsFull = false;
                break;
            }
        }
        return invIsFull;
    }

    public static String getMainhandItemDisplayName() {
        return mc.player.getHeldItemMainhand().getDisplayName();
    }

    public static int getFullInventorySlots() {
        /*
         Slot 9 is where the actual inventory starts, the other slots are the Crafting Grid and the Armor Inventory.
         44 is the last slot of the inventory, when no other GUI is open.
        */
        int fullSlots = 0;
        for(int i = 9; i < 45; i++) {
            if(!Game.getItemName(mc.player.inventoryContainer.inventorySlots.get(i).getStack().getItem()).equals("air"))
                fullSlots++;
        }
        return fullSlots;
    }

    public static int getEmptyInventorySlots(boolean includeNonFullSlots) {
        List<Slot> slotList = mc.player.inventoryContainer.inventorySlots;

        int emptySlots = 0;
        for(int i = 9; i < 45; i++) {
            Item item = slotList.get(i).getStack().getItem();
            int stackSize = slotList.get(i).getStack().getCount();
            int maxStackSize = slotList.get(i).getStack().getMaxStackSize();
            if(includeNonFullSlots) {
                if (Game.getItemName(item).equals("air") || stackSize < maxStackSize) {
                    emptySlots++;
                }
            } else if(Game.getItemName(item).equals("air")) {
                emptySlots++;
            }
        }
        return emptySlots;
    }

    public static int getItemStackSizeFromInventory(String itemToCheckFor, int limitSearch) {
        int stackSize = 0;
        for(int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
            ItemStack itemInIndex = mc.player.inventory.getStackInSlot(i);
            String displayName = StringUtils.stripControlCodes(itemInIndex.getDisplayName());
            if(displayName.equals(itemToCheckFor)) {
                stackSize += itemInIndex.getCount();

                /*
                 If no limit search parameter has been provided or if the provided is less than 0,
                 the executed actions will be handled as if there has no limit search parameter been provided at all.
                */
                if(limitSearch > -1 && (stackSize >= limitSearch)) break;
            }
        }
        return stackSize;
    }

    public static ItemStack getItemStackFromInventory(int slotId) {
        /* If slotId is bigger than 44, it's not an actual slot of the survivalInventory anymore, and it would just return air. */
        if (slotId >= 45)
            return null;
        List<Slot> itemStacks = mc.player.inventoryContainer.inventorySlots;
        return itemStacks.get(slotId).getStack();
    }


    /*
     The actual name for this method is mouseClick in the Minecraft Source Code, using the name slotClick for convenience.
     The method slotClick also exists in the Minecraft Source Code, but it's used differently than the slotclick actions provided by MacroMod.
     And since I need similar behaviour as the MacroMod slotclick simulates, this method exists.

     ARGS:
     arg1 -> The ID of the Slot which is being clicked
     arg2 -> Whether the Slot should be left- or right-clicked
    */
    public static void slotClick(int slotId, SlotClickType clickType) {
        GuiContainer guiContainer = mc.currentScreen instanceof GuiContainer ? (GuiContainer) mc.currentScreen : null;
        if(guiContainer == null)
            return;
        Container containerSlots = guiContainer.inventorySlots;
        Slot slot = containerSlots.getSlot(slotId);


        /*
         Translating the click type from the enum to the respective integer value
         due to Minecraft Source Code being retarded and using cryptic integer values instead of clear names
        */
        int clickTypeAsInt;
        if(clickType.equals(SlotClickType.LEFT_CLICK)) clickTypeAsInt = 0;
        else if(clickType.equals(SlotClickType.RIGHT_CLICK)) clickTypeAsInt = 1;
        else return;

        ((IGuiContainer) guiContainer).mouseClick(slot, slotId, clickTypeAsInt, ClickType.PICKUP);
    }

    public enum SlotClickType {
        LEFT_CLICK, RIGHT_CLICK
    }

    public static String getChestName() {
        if(!(mc.player.openContainer instanceof ContainerChest)) return null;
        ContainerChest chest = (ContainerChest) mc.player.openContainer;
        return chest.getLowerChestInventory().getDisplayName().getUnformattedText();
    }

    public static ItemStack getItemStackInSlot(int slotId){
        if (!(mc.currentScreen instanceof GuiContainer)) {
            return mc.player.inventory.getStackInSlot(slotId);
        }
        GuiContainer guiContainer = (GuiContainer) mc.currentScreen;
        Container chestAndSurvivalInventory = guiContainer.inventorySlots;
        List<Slot> inventorySlots = chestAndSurvivalInventory.inventorySlots;
        return inventorySlots.get(slotId).getStack();
    }

}













