package me.trojan.module.actions.gui.tablist;

import me.trojan.base.BaseScriptAction;
import me.trojan.helpers.GuiUtils;
import net.eq2online.macros.scripting.api.*;

public class GetTablistFooter extends BaseScriptAction {
    public GetTablistFooter() {
        super("gettablistfooter");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        String tablistFooter = GuiUtils.Tablist.getFooterComponent().getFormattedText();
        if(params.length > 0)
            provider.setVariable(macro, params[0], tablistFooter);
        return new ReturnValue(tablistFooter);
    }
}
