package me.trojan.module.actions.conditional;

import me.trojan.base.BaseConditionalScriptAction;
import me.trojan.helpers.ContainerUtils;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IScriptActionProvider;

public class IfInvIsNotFull extends BaseConditionalScriptAction {
    public IfInvIsNotFull() {
        super("ifInvIsNotFull");
    }

    @Override
    public boolean executeConditional(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        return !ContainerUtils.checkIfInvIsFull();
    }
}
