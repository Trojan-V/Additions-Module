package me.trojan.module.actions.gui;

import me.trojan.base.BaseScriptAction;
import me.trojan.helpers.HypixelLocationIdentifier;
import net.eq2online.macros.scripting.api.*;

public class GetLocation extends BaseScriptAction {
    public GetLocation() {
        super("getLocation");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        String hypixelLocation = HypixelLocationIdentifier.getHypixelLocation();
        if(params.length > 0)
            provider.setVariable(macro, params[0], hypixelLocation);
        return new ReturnValue(hypixelLocation);
    }
}
