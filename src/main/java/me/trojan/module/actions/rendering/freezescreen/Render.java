package me.trojan.module.actions.rendering.freezescreen;

import me.trojan.base.BaseScriptAction;
import me.trojan.helpers.ScreenFreezer;
import net.eq2online.macros.scripting.api.*;

public class Render extends BaseScriptAction {
    public Render() {
        super("render");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        ScreenFreezer.getInstance().enableRender();
        return new ReturnValue("");
    }
}
