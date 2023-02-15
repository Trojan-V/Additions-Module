package me.trojan.module.actions.gui.scoreboard;

import me.trojan.base.BaseScriptAction;
import me.trojan.helpers.GuiUtils;
import me.trojan.helpers.RegexUtils;
import net.eq2online.macros.scripting.api.*;
import net.minecraft.util.StringUtils;

public class GetBalance extends BaseScriptAction {
    public GetBalance() {
        super("getbalance");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        String scoreboard = GuiUtils.Scoreboard.getScoreboard();
        String matchedData = RegexUtils.findMatch(StringUtils.stripControlCodes(scoreboard), "Purse: ([\\d.,]+)", 1);
        if(params.length > 0)
            provider.setVariable(macro, params[0], matchedData);
        return new ReturnValue(matchedData);
    }
}
