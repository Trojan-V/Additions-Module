package me.trojan.module.actions.gui.inventory.getslotitem;

import me.trojan.base.BaseScriptAction;
import me.trojan.helpers.GetSlotItemUtils;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptCore;

public class GetSlotItemEnchants extends BaseScriptAction {
    public GetSlotItemEnchants() {
        super("getslotitemenchants");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(params.length == 0)
            return new ReturnValue("Not enough parameters have been provided, the minimum is 1 (parameter missing: int slotId).");

        int slotId = ScriptCore.tryParseInt(provider.expand(macro, params[0], false), -1);
        if(slotId == -1)
            return new ReturnValue("Invalid parameter for slotID.");

        GetSlotItemUtils slotUtils = new GetSlotItemUtils(slotId);

        if(params.length > 1)
            provider.setVariable(macro, params[1], slotUtils.getItemId());
        if(params.length > 2)
            provider.setVariable(macro, params[2], slotUtils.getItemStackSize());
        if(params.length > 3)
            provider.setVariable(macro, params[3], slotUtils.getItemMetadata());
        if(params.length > 4)
            provider.setVariable(macro, params[4], slotUtils.getItemEnchantments());

        return new ReturnValue(slotUtils.getItemEnchantments());
    }
}
