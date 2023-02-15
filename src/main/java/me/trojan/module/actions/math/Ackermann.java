package me.trojan.module.actions.math;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.exceptions.ScriptExceptionStackOverflow;
import net.eq2online.macros.scripting.parser.ScriptCore;

public class Ackermann extends BaseScriptAction {
    public Ackermann() {
        super("ackermann");
    }

    private int calculate(int m, int n) {
        if (m == 0) {
            return n + 1;
        } else if (m > 0 && n == 0) {
            return this.calculate(m - 1, 1);
        } else if (m > 0 && n > 0) {
            return this.calculate(m - 1, this.calculate(m, n - 1));
        }
        return 0;
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        int m = ScriptCore.tryParseInt(provider.expand(macro, params[0], false), 0);
        int n = ScriptCore.tryParseInt(provider.expand(macro, params[1], false), 0);

        int result;

        try {
            result = this.calculate(m, n);
        } catch (StackOverflowError err) {
            throw new ScriptExceptionStackOverflow();
        }
        return new ReturnValue(result);
    }
}
