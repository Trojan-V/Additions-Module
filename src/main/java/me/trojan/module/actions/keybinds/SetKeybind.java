package me.trojan.module.actions.keybinds;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.core.Macros;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptCore;

public class SetKeybind extends BaseScriptAction {
    private int keyID;

    public SetKeybind() {
        super("setKeybind");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(params.length <= 1)
            return new ReturnValue(false);
        keyID = ScriptCore.tryParseInt(provider.expand(macro, params[0], false), -1);
        if(isKeyIdInvalid())
            return new ReturnValue("Invalid Key-ID.");

        String codeForKeybind = provider.expand(macro, params[1], false);
        Macros.getInstance().getMacroTemplate(keyID).setKeyDownMacro(codeForKeybind);
        return new ReturnValue("Successfully set code to Keybind.");
    }

    private boolean isKeyIdInvalid() {
        return (keyID < 0) || (keyID > 9999);
    }
}
