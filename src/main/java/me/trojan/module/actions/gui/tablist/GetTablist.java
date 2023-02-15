package me.trojan.module.actions.gui.tablist;

import me.trojan.base.BaseScriptAction;
import me.trojan.helpers.GuiUtils;
import net.eq2online.macros.scripting.api.*;

public class GetTablist extends BaseScriptAction {
    public GetTablist() {
        super("gettablist");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        ReturnValueArray returnArray = new ReturnValueArray(true);
        returnArray.putStrings(GuiUtils.Tablist.getTablist());
        return returnArray;
    }
}