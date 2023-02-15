package me.trojan.module.actions.math;

import me.trojan.base.BaseScriptAction;
import me.trojan.helpers.ContainerUtils;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptCore;

public class GetItemStackSize extends BaseScriptAction {
    public GetItemStackSize() {
        super("getitemstacksize");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(params.length > 0) {
            String itemToCheckFor = provider.expand(macro, params[0], false);
            int limitSearch = -1;
            if (params.length > 1) limitSearch = ScriptCore.tryParseInt(provider.expand(macro, params[1], false), -1);

            int stackSize = ContainerUtils.getItemStackSizeFromInventory(itemToCheckFor, limitSearch);
            return new ReturnValue(String.valueOf(stackSize));
        }
        return null;
    }
}
