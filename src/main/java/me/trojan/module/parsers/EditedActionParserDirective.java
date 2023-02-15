package me.trojan.module.parsers;

import net.eq2online.macros.scripting.ActionParser;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IMacroActionProcessor;
import net.eq2online.macros.scripting.parser.ActionParserAbstract;
import net.eq2online.macros.scripting.parser.ScriptContext;

import java.util.regex.Matcher;

public class EditedActionParserDirective extends ActionParserAbstract {
    public EditedActionParserDirective(ScriptContext context) {
        super(context);
    }

    @Override
    public IMacroAction parse(IMacroActionProcessor actionProcessor, String scriptEntry) {
        Matcher directiveMatcher = ActionParser.PATTERN_DIRECTIVE.matcher(scriptEntry);
        return directiveMatcher.matches() ? this.parse(actionProcessor, directiveMatcher.group(1), "", null) : null;
    }
}
