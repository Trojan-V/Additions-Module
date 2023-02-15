package me.trojan.module.actions.math.usedByActionParser;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;

public class Divide extends BaseScriptAction {
    public Divide() {
        super("divide");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(params.length <= 1)
            return null;
        String varName = provider.expand(macro, params[0], false);

        /* Get the current value of the variable which is going to be multiplied by the supplied multiplier (params[1]) */
        Object variable = provider.getVariable(varName, macro);
        int currentVariableValue = (int) variable;

        int numberToDivideBy = Integer.parseInt(provider.expand(macro, params[1], false));
        int divisionResult = currentVariableValue / numberToDivideBy;
        provider.setVariable(macro, varName, divisionResult);
        return null;
    }
}
