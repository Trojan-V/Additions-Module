package me.trojan.module.iterators;

import me.trojan.base.BaseIterator;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.IScriptedIterator;
import net.eq2online.util.Game;
import net.minecraft.item.ItemStack;

public class Inventory extends BaseIterator {
    public Inventory() {
        super(null, null);
    }

    public Inventory(IScriptActionProvider provider, IMacro macro) {
        super(provider, macro);

        for(int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            begin();
            add("ITEMID", Game.getItemName(itemStack.getItem()));
            add("ITEMDISPLAYNAME", itemStack.getDisplayName());
            add("ITEMSTACKSIZE", itemStack.getCount());
            add("ITEMNBT", itemStack.getTagCompound() != null ? itemStack.getTagCompound().toString() : "unknownTag");
            add("ITEMDATAVAR", itemStack.getMetadata());
            add("ITEMDAMAGE", itemStack.getItemDamage());
            add("ITEMMAXSTACKSIZE", itemStack.getMaxStackSize());
            end();
        }
    }

    @Override
    public String getIteratorName() {
        return "inventory";
    }

    @Override
    public Class<? extends IScriptedIterator> getIteratorClass() {
        return getClass();
    }
}
