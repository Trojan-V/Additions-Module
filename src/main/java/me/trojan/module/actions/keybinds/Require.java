package me.trojan.module.actions.keybinds;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.core.MacroTemplate;
import net.eq2online.macros.core.Macros;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptCore;

public class Require extends BaseScriptAction {
    private int keyID;

    public Require() {
        super("require");
    }

    /*
     require(<keyID>,<CTRL/SHIFT/ALT>,<true/false>);
     true/false enabling or disabling the modifier.
    */
    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(params.length <= 1)
            return new ReturnValue(false);
        keyID = ScriptCore.tryParseInt(provider.expand(macro, params[0], false), -1);
        if(isKeyIdInvalid())
            return new ReturnValue("Invalid Key-ID.");

        String requirement = provider.expand(macro, params[1], false);
        if(!requirement.toUpperCase().matches("CTRL|ALT|SHIFT"))
            return new ReturnValue("The second parameter is not a valid argument for this actions.");

        boolean enableRequirement = true;
        if(params.length > 2) enableRequirement = Boolean.parseBoolean(provider.expand(macro, params[2], false));

        MacroTemplate template = Macros.getInstance().getMacroTemplate(keyID);
        switch(requirement.toUpperCase()) {
            case "ALT":
                template.requireAlt = enableRequirement;
                break;
            case "SHIFT":
                template.requireShift = enableRequirement;
                break;
            case "CONTROL":
            case "CTRL":
                template.requireControl = enableRequirement;
                break;
        }
        if(enableRequirement)
            return new ReturnValue("The requirement has been added to the provided Key-ID.");
        return new ReturnValue("The requirement has been removed from the provided Key-ID.");
    }

    private boolean isKeyIdInvalid() {
        return (keyID < 0) || (keyID > 9999);
    }
}
