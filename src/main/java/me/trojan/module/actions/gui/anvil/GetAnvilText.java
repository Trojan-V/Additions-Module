package me.trojan.module.actions.gui.anvil;

import me.trojan.base.BaseScriptAction;
import me.trojan.helpers.GuiUtils;
import net.eq2online.macros.scripting.api.*;

public class GetAnvilText extends BaseScriptAction {
    public GetAnvilText() {
        super("getanviltext");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        String anvilText = GuiUtils.Anvil.getAnvilText();
        if(params.length > 0)
            provider.setVariable(macro, params[0], anvilText);
        return new ReturnValue(anvilText);
    }
}
