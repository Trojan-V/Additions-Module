package me.trojan.module.actions.gui.inventory;

import me.trojan.base.BaseScriptAction;
import me.trojan.helpers.ContainerUtils;
import net.eq2online.macros.scripting.api.*;

public class GetEmptySlots extends BaseScriptAction {
    public GetEmptySlots() {
        super("getemptyslots");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        boolean includeNonFullSlots = false;
        if(params.length > 0)
            includeNonFullSlots = Boolean.parseBoolean(provider.expand(macro, params[0], false));
        if(params.length > 1)
            provider.setVariable(macro, params[1], ContainerUtils.getEmptyInventorySlots(includeNonFullSlots));

        return new ReturnValue(ContainerUtils.getEmptyInventorySlots(includeNonFullSlots));
    }
}
