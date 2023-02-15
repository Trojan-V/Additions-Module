package me.trojan.module.actions.system;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.util.Game;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.text.ITextComponent;

public class TakeScreenshot extends BaseScriptAction {
    public TakeScreenshot() {
        super("takeScreenshot");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        ITextComponent screenshotMessage = ScreenShotHelper.saveScreenshot(this.mc.mcDataDir, this.mc.displayWidth, this.mc.displayHeight, this.mc.getFramebuffer());
         if (params.length > 0 && Boolean.parseBoolean(provider.expand(macro, params[0], false)))
             Game.addChatMessage(screenshotMessage.getFormattedText());
        return new ReturnValue(screenshotMessage.getFormattedText());
    }
}
