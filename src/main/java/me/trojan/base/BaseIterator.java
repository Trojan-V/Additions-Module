package me.trojan.base;

import net.eq2online.macros.scripting.ScriptedIterator;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.IScriptedIterator;
import net.eq2online.macros.scripting.parser.ScriptContext;

public abstract class BaseIterator extends ScriptedIterator {
    public BaseIterator(IScriptActionProvider provider, IMacro macro) {
        super(provider, macro);
    }

    public abstract String getIteratorName();
    public abstract Class<? extends IScriptedIterator> getIteratorClass();

    @Override
    public void onInit() {
        for(ScriptContext context : ScriptContext.getAvailableContexts()) {
            context.getCore().registerIterator(getIteratorName(), getIteratorClass());
        }
    }
}
