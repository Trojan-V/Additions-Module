package me.trojan.base;

import net.eq2online.macros.scripting.actions.lang.ScriptActionIf;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptContext;

public abstract class BaseConditionalScriptAction extends ScriptActionIf {
    public BaseConditionalScriptAction(String actionName) {
        super(ScriptContext.MAIN, actionName);
    }

    @Override
    public abstract boolean executeConditional(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params);

    @Override
    public boolean isConditionalOperator() {
        return true;
    }

    @Override
    public void onInit() {
        for(ScriptContext context : ScriptContext.getAvailableContexts()) {
            context.getCore().registerScriptAction(this);
        }
    }
}
