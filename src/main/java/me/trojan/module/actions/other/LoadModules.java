package me.trojan.module.actions.other;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.ModuleLoader;
import net.eq2online.macros.scripting.api.*;

public class LoadModules extends BaseScriptAction {
    public LoadModules() {
        super("loadModules");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        new ModuleLoader(macros.getMacrosDirectory()).loadModules(macros);
        return new ReturnValue(true);
    }
}
