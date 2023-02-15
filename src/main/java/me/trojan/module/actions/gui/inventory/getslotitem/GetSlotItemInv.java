package me.trojan.module.actions.gui.inventory.getslotitem;

import me.trojan.base.BaseScriptAction;
import me.trojan.helpers.ContainerUtils;
import me.trojan.helpers.GetSlotItemUtils;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.minecraft.item.ItemStack;

public class GetSlotItemInv extends BaseScriptAction {
    public GetSlotItemInv() {
        super("getslotiteminv");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(params.length == 0)
            return new ReturnValue("Not enough parameters have been provided, the minimum is 1 (parameter missing: int slotID).");

        int slotID = ScriptCore.tryParseInt(provider.expand(macro, params[0], false), -1);
        if(slotID == -1 || slotID >= 45)
            return new ReturnValue("An invalid Slot ID has been provided.");

        ItemStack itemStack = ContainerUtils.getItemStackFromInventory(slotID);
        GetSlotItemUtils slotUtils = new GetSlotItemUtils(itemStack);

        if(params.length > 1)
            provider.setVariable(macro, provider.expand(macro, params[1], false), slotUtils.getItemId());
        if(params.length > 2)
            provider.setVariable(macro, provider.expand(macro, params[2], false), slotUtils.getItemStackSize());
        if(params.length > 3)
            provider.setVariable(macro, provider.expand(macro, params[3], false), slotUtils.getItemMetadata());
        if(params.length > 4)
            provider.setVariable(macro, provider.expand(macro, params[4], false), slotUtils.getItemDisplayName());
        if(params.length > 5)
            provider.setVariable(macro, provider.expand(macro, params[5], false), slotUtils.getItemNbtTag());

        return new ReturnValue(slotUtils.getItemId());
    }
}
