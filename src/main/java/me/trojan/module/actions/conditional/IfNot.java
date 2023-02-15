package me.trojan.module.actions.conditional;

import me.trojan.base.BaseConditionalScriptAction;
import net.eq2online.macros.scripting.api.IExpressionEvaluator;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IScriptActionProvider;

public class IfNot extends BaseConditionalScriptAction {
    public IfNot() {
        super("ifNot");
    }

    @Override
    public boolean executeConditional(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        String condition = params.length > 0 ? params[0] : "flag";
        IExpressionEvaluator evaluator = provider.getExpressionEvaluator(macro, provider.expand(macro, condition, true));
        return evaluator.evaluate() == 0;
    }
}
