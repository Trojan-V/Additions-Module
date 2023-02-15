package me.trojan.module.actions.switchcase;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;

public class Default extends BaseScriptAction {
    public Default() {
        super("default");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        return null;
    }

    @Override
    public boolean isStackPopOperator() {
        return true;
    }

    @Override
    public boolean isConditionalElseOperator(IScriptAction action) {
        return (action instanceof Switch);
    }

    @Override
    public void executeConditionalElse(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params, IMacroActionStackEntry top) {
        top.setConditionalFlag(!top.getIfFlag());
        top.setElseFlag(true);
    }
}
