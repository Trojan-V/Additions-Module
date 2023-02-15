package me.trojan.module.actions.switchcase;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;

public class Switch extends BaseScriptAction {
    public Switch() {
        super("switch");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        return null;
    }

    @Override
    public boolean isConditionalOperator() {
        return true;
    }

    @Override
    public boolean executeConditional(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        return false;
    }
}
