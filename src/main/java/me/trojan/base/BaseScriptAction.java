package me.trojan.base;

import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;

public abstract class BaseScriptAction extends ScriptAction {

    public BaseScriptAction(String actionName) {
        super(ScriptContext.MAIN, actionName);
    }

    @Override
    public void onInit() {
        for (ScriptContext context : ScriptContext.getAvailableContexts()) {
            context.getCore().registerScriptAction(this);
        }
    }

    @Override
    public boolean isPermissable() {
        return false;
    }

    public abstract IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params);

}
