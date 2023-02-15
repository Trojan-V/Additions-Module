package me.trojan.module.actions.gui.tablist;

import me.trojan.base.BaseScriptAction;
import me.trojan.helpers.GuiUtils;
import net.eq2online.macros.scripting.api.*;

public class GetTablistHeader extends BaseScriptAction {
    public GetTablistHeader() {
        super("gettablistheader");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        String tablistHeader = GuiUtils.Tablist.getHeaderComponent().getFormattedText();
        if(params.length > 0)
            provider.setVariable(macro, params[0], tablistHeader);
        return new ReturnValue(tablistHeader);
    }
}
