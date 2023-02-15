package me.trojan.module.actions.bossbar;

import me.trojan.base.BaseScriptAction;
import me.trojan.helpers.GuiUtils;
import net.eq2online.macros.scripting.api.*;

public class GetBossBarColor extends BaseScriptAction {
    public GetBossBarColor() {
        super("getBossBarColor");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        ReturnValueArray returnValueArray = new ReturnValueArray(false);
        returnValueArray.putStrings(GuiUtils.BossBar.getBossBarColor());
        return returnValueArray;
    }
}
