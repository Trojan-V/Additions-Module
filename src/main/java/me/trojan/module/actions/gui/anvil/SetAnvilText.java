package me.trojan.module.actions.gui.anvil;

import me.trojan.base.BaseScriptAction;
import me.trojan.helpers.GuiUtils;
import net.eq2online.macros.scripting.api.*;

public class SetAnvilText extends BaseScriptAction {
    public SetAnvilText() {
        super("setanviltext");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(params.length == 0)
            return new ReturnValue(false);
        String anvilText = provider.expand(macro, params[0], false);
        GuiUtils.Anvil.setAnvilText(anvilText);
        return new ReturnValue(true);
    }
}

