package me.trojan.module.actions.switchcase;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;

public class EndSwitch extends BaseScriptAction {
    public EndSwitch() {
        super("endSwitch");
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
    public boolean matchesConditionalOperator(IScriptAction action) {
        return action instanceof Switch;
    }
}
