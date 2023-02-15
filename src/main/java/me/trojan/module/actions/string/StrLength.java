package me.trojan.module.actions.string;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;

public class StrLength extends BaseScriptAction {
    public StrLength() {
        super("strLength");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        String input = provider.expand(macro, params[0], false);
        int length = input.length() + 1;
        if(params.length > 1)
            provider.setVariable(macro, params[1], length);
        return new ReturnValue(length);
    }
}
