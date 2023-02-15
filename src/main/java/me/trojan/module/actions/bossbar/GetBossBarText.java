package me.trojan.module.actions.bossbar;

import me.trojan.base.BaseScriptAction;
import me.trojan.helpers.GuiUtils;
import net.eq2online.macros.scripting.api.*;

public class GetBossBarText extends BaseScriptAction {
    public GetBossBarText() {
        super("getBossBarText");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        ReturnValueArray returnValueArray = new ReturnValueArray(false);
        returnValueArray.putStrings(GuiUtils.BossBar.getBossBarText());
        return returnValueArray;
    }
}
