package me.trojan.module.actions.better;

import net.eq2online.macros.gui.interfaces.IContentRenderer;
import net.eq2online.macros.gui.screens.GuiMacroParam;
import net.eq2online.macros.scripting.PromptTarget;
import net.eq2online.macros.scripting.actions.lang.ScriptActionPrompt;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.interfaces.IPromptOverridable;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.util.Util;
import net.minecraft.util.StringUtils;

public class BetterPrompt extends ScriptActionPrompt {
    public BetterPrompt() {
        super(ScriptContext.MAIN);
    }

    @Override
    public boolean canExecuteNow(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        PromptTarget state;
        if (instance.getState() != null) {
            state = instance.getState();
            return state != null && state.getCompleted();
        }

        if (params.length < 2)
            return true;

        // prompt(&var,"$$[select[Hello,Bye]]","Enter the message you want here.",true/false,<default>)
        // prompt(&var,"$$[select[Hello,Bye]]","Enter the message you want here.",true,%&var%)
        String prompt = provider.expand(macro, params[1], false);
        String strippedPrompt = Util.convertAmpCodes(prompt);
        String promptSource = params.length > 2 ? provider.expand(macro, params[2], false) : null;
        String defaultValue = params.length > 4 ? provider.expand(macro, params[4], false) : "";

        state = new PromptTarget(macros, mc, macro, strippedPrompt, promptSource, defaultValue, mc.currentScreen);

        /* During my testings, state.getCompleted() always returned false here, hence why this if-statement has always been executed. */
        if (!state.getCompleted()) {
            String overrideParam = params.length > 3 ? provider.expand(macro, params[3], false) : null;
            boolean allowOverride = ("1".equals(overrideParam) ||
                    "true".equalsIgnoreCase(overrideParam) || "yes".equalsIgnoreCase(overrideParam));

            instance.setState(state);

            /* This if-block was never called during my tests. */
            if (mc.currentScreen != null && !(mc.currentScreen instanceof IPromptOverridable) && !allowOverride) {
                provider.setVariable(macro, provider.expand(macro, params[0], false), "");
                return true;
            }

            /* During my tests, the else block was always called and never the if-block. */
            if (mc.currentScreen instanceof IPromptOverridable) {
                IContentRenderer contentRenderer = ((IPromptOverridable)mc.currentScreen).getContentRenderer();
                mc.displayGuiScreen(new GuiMacroParam<>(macros, mc, state, contentRenderer));
            } else {
                mc.displayGuiScreen(new GuiMacroParam<>(macros, mc, state));
            }
            return false;
        }
        /* state.getCompleted() never returned true during my tests, hence why this part has never been reached. */
        //Game.addChatMessage("return state.getCompleted(): " + state.getCompleted());
        return state.getCompleted();
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        PromptTarget state = instance.getState();
        /* This if-statement has never been executed during my testings. */
        if (state == null || !state.getCompleted())
            return new ReturnValue("");

        /* Example: prompt(&var,"$$[select[Hello,Bye]]","Enter the message you want here.",true,%&var%)
         variableName = &var
         value = "Hello" or "Bye" depending on the choice which has been made within the prompt.
         If nothing has been chosen within the prompt (ESC has been pressed),
         the default value will be inserted (from 5th parameter). here: %&var% which assigns the previous value this variable held. */
        String variableName = provider.expand(macro, params[0], false);
        String value = state.getSuccess() ? state.getTargetString() : state.getDefaultValue();
        provider.setVariable(macro, variableName, StringUtils.stripControlCodes(value));
        return new ReturnValue(value);
    }

    @Override
    public void onInit() {
        for(ScriptContext context : ScriptContext.getAvailableContexts()) {
            context.getCore().registerScriptAction(this);
        }
    }
}
