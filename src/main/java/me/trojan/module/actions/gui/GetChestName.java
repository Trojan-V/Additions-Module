package me.trojan.module.actions.gui;

import me.trojan.base.BaseScriptAction;
import me.trojan.helpers.ContainerUtils;
import net.eq2online.macros.scripting.api.*;

public class GetChestName extends BaseScriptAction {

    public GetChestName() {
        super("getchestname");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        String chestName = ContainerUtils.getChestName();
        if(params.length > 0)
            provider.setVariable(macro, params[0], chestName);
        return new ReturnValue(chestName);
    }
}