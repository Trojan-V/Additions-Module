package me.trojan.module.actions.system;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.minecraft.client.gui.GuiCustomizeSkin;

public class SetClipboard extends BaseScriptAction {

    public SetClipboard() {
        super("setClipboard");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        GuiCustomizeSkin.setClipboardString(provider.expand(macro, params[0], false));
        return null;
    }
}
