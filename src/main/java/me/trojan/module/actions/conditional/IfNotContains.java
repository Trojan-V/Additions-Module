package me.trojan.module.actions.conditional;

import me.trojan.base.BaseConditionalScriptAction;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IScriptActionProvider;

public class IfNotContains extends BaseConditionalScriptAction {
    public IfNotContains() {
        super("ifNotContains");
    }

    @Override
    public boolean executeConditional(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        String haystack = provider.expand(macro, params[0], false);
        String needle = provider.expand(macro, params[1], false);
        return !haystack.contains(needle);
    }
}
