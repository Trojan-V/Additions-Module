package me.trojan.module.parsers;

import net.eq2online.macros.scripting.ActionParser;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IMacroActionProcessor;
import net.eq2online.macros.scripting.parser.ActionParserAbstract;
import net.eq2online.macros.scripting.parser.ScriptContext;

import java.util.regex.Matcher;

public class EditedActionParserAction extends ActionParserAbstract {
    public EditedActionParserAction(ScriptContext context) {
        super(context);
    }

    @Override
    public IMacroAction parse(IMacroActionProcessor actionProcessor, String scriptEntry) {
        Matcher scriptActionPatternMatcher = ActionParser.PATTERN_SCRIPTACTION.matcher(scriptEntry);
        if (!scriptActionPatternMatcher.matches())
            return null;

        String actionName = scriptActionPatternMatcher.group(1);
        String params = scriptActionPatternMatcher.group(2);
        return this.parse(actionProcessor, actionName, params, null);
    }
}
