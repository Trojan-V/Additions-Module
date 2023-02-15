package me.trojan.module.actions.string;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;

public class Trim extends BaseScriptAction {
    public Trim() {
        super("trim");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        String trimmedString = "";
        if(params.length > 0) trimmedString = provider.expand(macro, params[0], false).trim();
        if(params.length > 1) provider.setVariable(macro, params[1], trimmedString);
        return new ReturnValue(trimmedString);
    }
}
