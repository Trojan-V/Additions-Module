package me.trojan.module.actions.conditional;

import me.trojan.base.BaseConditionalScriptAction;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptCore;

public class IfRunning extends BaseConditionalScriptAction {
    public IfRunning() {
        super("ifRunning");
    }

    @Override
    public boolean executeConditional(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(params.length < 1)
            return false;

        String macroTaskName = provider.expand(macro, params[0], false).trim().toLowerCase();
        int macroIntValue = ScriptCore.tryParseInt(macroTaskName, 0);

        for (IMacro.IMacroStatus status : provider.getMacroEngine().getExecutingMacroStatus()) {
            String currentName = status.getMacro().getDisplayName().toLowerCase();
            int currentIntValue = status.getMacro().getID();

            if (currentName.equals(macroTaskName) || (macroIntValue > 0 && currentIntValue == macroIntValue))
                return true;
        }
        return false;
    }
}
