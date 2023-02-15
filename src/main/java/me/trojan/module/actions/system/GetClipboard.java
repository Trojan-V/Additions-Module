package me.trojan.module.actions.system;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;
import net.minecraft.client.gui.GuiCustomizeSkin;

public class GetClipboard extends BaseScriptAction {

    public GetClipboard() {
        super("getClipboard");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(params.length > 0)
            provider.setVariable(macro, params[0], GuiCustomizeSkin.getClipboardString());
        return new ReturnValue(GuiCustomizeSkin.getClipboardString());
    }
}
