package me.trojan.module.actions.math;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptCore;

public class Trunc extends BaseScriptAction {
    public Trunc() {
        super("trunc");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        float number = ScriptCore.tryParseFloat(provider.expand(macro, params[0], false), 0F);
        if (number != 0) {
            number -= number % 1;
        }
        return new ReturnValue((int) number);
    }
}
