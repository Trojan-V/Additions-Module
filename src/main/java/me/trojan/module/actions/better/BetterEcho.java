package me.trojan.module.actions.better;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;

public class BetterEcho extends BaseScriptAction {
    public BetterEcho() {
        super("echo");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        String message = params.length > 0 ? provider.expand(macro, params[0], false) : "";
        mc.player.sendChatMessage(message);
        return new ReturnValue("");
    }
}
