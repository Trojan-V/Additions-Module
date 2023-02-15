package me.trojan.module.actions.math;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.minecraft.item.ItemStack;

public class GetSlotPositions extends BaseScriptAction {
    public GetSlotPositions() {
        super("getSlotPositions");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        String itemToCheckFor = provider.expand(macro, params[0], false);
        /*
         params[1] --> Array which holds the values of the found slotID's.
         params[2] --> Array which holds the values of the stackSize's.
        */

        provider.clearArray(macro, params[1]);
        if(params.length > 2) provider.clearArray(macro, params[2]);

        for(int i = 0; i < this.mc.player.inventory.getSizeInventory(); i++) {
            ItemStack itemInIndex = this.mc.player.inventory.getStackInSlot(i);
            if(itemInIndex.getDisplayName().equals(itemToCheckFor)) {
                provider.putValueToArray(macro, params[1], String.valueOf(i));
                if(params.length > 2) {
                    provider.putValueToArray(macro, params[2], String.valueOf(itemInIndex.getCount()));
                }
            }
        }
        return null;
    }
}
