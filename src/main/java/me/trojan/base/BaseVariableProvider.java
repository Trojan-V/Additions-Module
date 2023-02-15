package me.trojan.base;

import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.variable.VariableCache;

public abstract class BaseVariableProvider extends VariableCache {

    @Override
    public Object getVariable(String variableName) {
        return this.getCachedValue(variableName);
    }

    @Override
    public void onInit() {
        for(ScriptContext context : ScriptContext.getAvailableContexts()) {
            context.getCore().registerVariableProvider(this);
        }
    }
}
