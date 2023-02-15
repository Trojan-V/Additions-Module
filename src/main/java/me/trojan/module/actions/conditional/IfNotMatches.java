package me.trojan.module.actions.conditional;

import me.trojan.base.BaseConditionalScriptAction;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IScriptActionProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class IfNotMatches extends BaseConditionalScriptAction {
    public IfNotMatches() {
        super("ifNotMatches");
    }

    @Override
    public boolean executeConditional(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(params.length <= 1)
            return false;

        String subject = provider.expand(macro, params[0], false);
        String pattern = provider.expand(macro, params[1], false);

        try {
            Pattern regex = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            Matcher matcher = regex.matcher(subject);
            if (!matcher.find())
                return true;
        } catch(PatternSyntaxException ex) {
            displayErrorMessage(provider, macro, instance, ex, "script.error.badregex");
        }

        return false;
    }
}
