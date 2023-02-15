package me.trojan.module.actions.keybinds;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.core.Macros;
import net.eq2online.macros.scripting.api.*;

public class GetKeybind extends BaseScriptAction {
    private int keyID;

    public GetKeybind() {
        super("getKeybind");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(params.length == 0)
            return new ReturnValue(false);

        keyID = Integer.parseInt(provider.expand(macro, params[0], false));
        if(isKeyIdInvalid())
            return new ReturnValue("Invalid Key-ID.");

        String keyDownMacro = Macros.getInstance().getMacroTemplate(keyID).getKeyDownMacro();
        if(params.length > 1)
            provider.setVariable(macro, params[1], keyDownMacro);
        return new ReturnValue(keyDownMacro);
    }

    private boolean isKeyIdInvalid() {
        return (keyID < 0) || (keyID > 9999);
    }
}
