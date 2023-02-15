package me.trojan.module.actions.gui.scoreboard;

import me.trojan.base.BaseScriptAction;
import me.trojan.helpers.GuiUtils;
import net.eq2online.macros.scripting.api.*;

public class GetScoreboard extends BaseScriptAction {
    public GetScoreboard() {
        super("getscoreboard");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        String scoreboard = GuiUtils.Scoreboard.getScoreboard();
        if(params.length > 0)
            provider.setVariable(macro, params[0], scoreboard);
        return new ReturnValue(scoreboard);
    }
}
