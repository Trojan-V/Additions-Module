package me.trojan.module.actions.rendering.freezescreen;

import me.trojan.base.BaseScriptAction;
import me.trojan.helpers.ScreenFreezer;
import net.eq2online.macros.scripting.api.*;

public class Unrender extends BaseScriptAction {
    public Unrender() {
        super("unrender");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        ScreenFreezer.getInstance().disableRender();
        return new ReturnValue("");
    }
}
