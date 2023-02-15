package me.trojan.module.actions.math.usedByActionParser;

import me.trojan.base.BaseScriptAction;
import me.trojan.engine.EditedExpressionEvaluator;
import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.api.*;

public class EditedAssign extends BaseScriptAction {
    public EditedAssign() {
        super("editedAssign");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        String variableName = params.length > 0 ? provider.expand(macro, params[0], false) : "flag";
        String variableValue = params.length > 1 ? params[1] : "";
        if (variableValue != null) {
            if (!variableName.startsWith("&") && !variableName.startsWith("@&")) {
                EditedExpressionEvaluator evaluator = new EditedExpressionEvaluator(provider.expand(macro, variableValue, true), provider, macro);
                variableValue = String.valueOf(evaluator.evaluate());
            } else {
                variableValue = provider.expand(macro, variableValue, false);
            }
        }

        if (Variable.couldBeArraySpecifier(variableName) && Variable.getValidVariableOrArraySpecifier(variableName) != null) {
            provider.pushValueToArray(macro, Variable.getValidVariableOrArraySpecifier(variableName), variableValue);
        } else {
            provider.setVariable(macro, variableName, variableValue);
        }

        return new ReturnValue(variableValue);
    }
}
