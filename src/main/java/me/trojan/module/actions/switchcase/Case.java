package me.trojan.module.actions.switchcase;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;

public class Case extends BaseScriptAction {
    public Case() {
        super("case");
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
        return action instanceof Switch;
    }

    @Override
    public void executeConditionalElse(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params, IMacroActionStackEntry top) {
        if(top.getIfFlag()) {
            top.setConditionalFlag(false);
            return;
        }

        String switchParam = top.getAction().getParams()[0];
        String value = provider.getVariable(switchParam, macro).toString();

        for(String param : params) {
            String parsed = provider.expand(macro, param, false);
            if(parsed.equals(value)) {
                top.setConditionalFlag(true);
                top.setIfFlag(true);
                return;
            }
        }
        top.setConditionalFlag(false);
    }
}
