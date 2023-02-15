package me.trojan.module.actions.math;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;

public class GetPercentage extends BaseScriptAction {

    public GetPercentage() {
        super("getPercentage");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        double percentageResult = Double.parseDouble(provider.expand(macro, params[0], false)) / Double.parseDouble(provider.expand(macro, params[1], false)) * 100;
        if(params.length > 2)
            provider.setVariable(macro, params[2], String.valueOf(percentageResult));
        return new ReturnValue(String.valueOf(percentageResult));
    }
}
