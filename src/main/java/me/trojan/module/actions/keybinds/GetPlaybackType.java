package me.trojan.module.actions.keybinds;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.core.Macros;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptCore;

public class GetPlaybackType extends BaseScriptAction {
    private int keyID;

    public GetPlaybackType() {
        super("getplaybacktype");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(params.length == 0)
            return new ReturnValue(false);
        keyID = ScriptCore.tryParseInt(provider.expand(macro, params[0], false), -1);
        if(isKeyIdInvalid())
            return new ReturnValue("Invalid Key-ID.");

        String playbackType = Macros.getInstance().getMacroTemplate(keyID).getPlaybackType().name();
        if(params.length > 1)
            provider.setVariable(macro, params[1], playbackType);
        return new ReturnValue(playbackType);
    }

    public boolean isKeyIdInvalid() {
        return (keyID < 0) || (keyID > 9999);
    }
}
