package me.trojan.helpers;

import net.eq2online.util.Game;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.List;
import java.util.Objects;

public class GetSlotItemUtils {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final ItemStack itemStack;
    private boolean slotIDIsInvalid = false;
    private int slotID;

    public GetSlotItemUtils(int slotID) {
        this.slotID = slotID;
        this.itemStack = getItemStack();
        if(isSlotIdInvalid())
            slotIDIsInvalid = true;
    }

    public GetSlotItemUtils(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public String getItemId() {
        if(slotIDIsInvalid)
            return "unknown";
        if(itemStack == ItemStack.EMPTY)
            return Game.getItemName(null);
        return Game.getItemName(itemStack.getItem());
    }

    public String getItemDisplayName() {
        if(slotIDIsInvalid)
            return "unknownName";
        return itemStack.getDisplayName();
    }

    public int getItemStackSize() {
        if(slotIDIsInvalid || itemStack == ItemStack.EMPTY)
            return 0;
        return itemStack.getCount();
    }

    public int getItemMetadata() {
        if(slotIDIsInvalid)
            return 0;
        if(itemStack == ItemStack.EMPTY)
            return 0;
        return itemStack.getMetadata();
    }

    public String getItemNbtTag() {
        if(slotIDIsInvalid)
            return "unknownTag";
        if(itemStack == ItemStack.EMPTY)
            return "noTag";
        return Objects.requireNonNull(itemStack.getTagCompound()).toString();
    }

    // TODO: Fix getItemStack private method to be the exact equivalent of slotHelper#getSlotStack(int slotId)
    public String getItemEnchantments() {
        Enchantment enchantment;
        NBTTagList nbtTagList;
        if(getItemId().equalsIgnoreCase("enchanted_book"))
            nbtTagList = ItemEnchantedBook.getEnchantments(itemStack);
        else
            nbtTagList = itemStack.getEnchantmentTagList();

        StringBuilder enchantmentString = new StringBuilder();
        for (int j = 0; j < nbtTagList.tagCount(); j++) {
            NBTTagCompound nbtTagCompound = nbtTagList.getCompoundTagAt(j);
            int k = nbtTagCompound.getShort("id");
            int l = nbtTagCompound.getShort("lvl");
            enchantment = Enchantment.getEnchantmentByID(k);
            if(enchantment == null)
                continue;

            // TODO: Remove last comma after this string has been created completely (when for loop ends basically, remove the last char.)
            enchantmentString.append(enchantment.getTranslatedName(l)).append(",");
        }
        return enchantmentString.toString().equals("") ? "unknownEnchants" : enchantmentString.toString();
    }


    private ItemStack getItemStack() {
        /* slotID - 1 to ensure that the slots in mkb are 1-9 and not 0-8 indexed. */
        if(!isCurrentScreenContainer())
            return mc.player.inventory.getStackInSlot(slotID - 1);

        Container survivalInventory = getGuiContainer().inventorySlots;
        List<Slot> inventorySlots = survivalInventory.inventorySlots;
        return inventorySlots.get(slotID).getStack();
    }

    private boolean isSlotIdInvalid() {
        /*
         If it evaluates to false, meaning there is currently no GUI opened
         the Container Size will be set to 9 because the amount of Hotbar Slots which are always available is 9.
        */
        int containerSize = isCurrentScreenContainer() ? getContainerSize() : 9;
        return slotID > containerSize || slotID < 0;
    }

    private GuiContainer getGuiContainer() {
        return isCurrentScreenContainer() ? (GuiContainer) mc.currentScreen : null;
    }

    private int getContainerSize() {
        if (!isCurrentScreenContainer())
            return 0;
        GuiContainer containerGui = getGuiContainer();
        return containerGui instanceof GuiContainerCreative ? 600 : containerGui.inventorySlots.inventorySlots.size();
    }

    private boolean isCurrentScreenContainer() {
        return mc.currentScreen instanceof GuiContainer;
    }
}
