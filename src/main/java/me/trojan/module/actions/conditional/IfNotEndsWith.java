package me.trojan.module.actions.conditional;

import me.trojan.base.BaseConditionalScriptAction;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IScriptActionProvider;

public class IfNotEndsWith extends BaseConditionalScriptAction {
    public IfNotEndsWith() {
        super("ifNotEndsWith");
    }

    @Override
    public boolean executeConditional(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(params.length <= 1)
            return false;
        String haystack = provider.expand(macro, params[0], false).toLowerCase().trim();
        String needle = provider.expand(macro, params[1], false).toLowerCase().trim();
        return !haystack.endsWith(needle);
    }
}
