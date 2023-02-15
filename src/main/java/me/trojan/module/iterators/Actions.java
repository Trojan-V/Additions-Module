package me.trojan.module.iterators;

import me.trojan.base.BaseIterator;
import net.eq2online.macros.scripting.IDocumentationEntry;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptAction;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.IScriptedIterator;
import net.eq2online.macros.scripting.parser.ScriptContext;

public class Actions extends BaseIterator {

    public Actions() {
        super(null, null);
    }

    public Actions(IScriptActionProvider provider, IMacro macro) {
        super(provider, macro);

        for (IScriptAction action : ScriptContext.MAIN.getCore().getActionsList()) {
            begin();
            IDocumentationEntry actionDoc = ScriptContext.MAIN.getCore().getDocumentor().getDocumentation(action);
            add("NAME", action.getName());
            add("USAGE", actionDoc != null ? actionDoc.getUsage() : "");
            add("RETURNTYPE", actionDoc != null ? actionDoc.getReturnType() : "");
            add("DESCRIPTION", actionDoc != null ? actionDoc.getDescription() : "");
            add("ISHIDDEN", actionDoc != null ? actionDoc.isHidden() : "");
            end();
        }
    }

    @Override
    public String getIteratorName() {
        return "actions";
    }

    @Override
    public Class<? extends IScriptedIterator> getIteratorClass() {
        return getClass();
    }
}
