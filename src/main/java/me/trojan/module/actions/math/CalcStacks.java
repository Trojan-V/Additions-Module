package me.trojan.module.actions.math;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptCore;

public class CalcStacks extends BaseScriptAction {
    public CalcStacks() {
        super("calcstacks");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        /*
         Takes an amount of items in form of an integer as input and calculates the amount of stacks the items are equal to
         Optionally returns the leftovers of a "started" but not "full" stack
        */
        int count = ScriptCore.tryParseInt(provider.expand(macro, params[0], false), 0);
        int stacks = count / 64;
        int leftovers = count % 64;

        if (params.length > 1) provider.setVariable(macro, params[1], stacks);
        if (params.length > 2) provider.setVariable(macro, params[2], leftovers);
        return new ReturnValue(stacks);
    }
}
