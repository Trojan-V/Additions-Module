package me.trojan.module.parsers;

import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IMacroActionProcessor;
import net.eq2online.macros.scripting.parser.ActionParserAbstract;
import net.eq2online.macros.scripting.parser.ScriptContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionParserInc extends ActionParserAbstract {
    private static final Pattern INC_PATTERN = Pattern.compile("(#[a-z][a-z0-9_]*)\\+{2}");

    public ActionParserInc(ScriptContext context) {
        super(context);
    }

    @Override
    public IMacroAction parse(IMacroActionProcessor actionProcessor, String scriptEntry) {
        Matcher m = INC_PATTERN.matcher(scriptEntry);
        if(!m.matches())
            return null;
        String varIdentifier = m.group(1);
        return this.parse(actionProcessor, "INC", varIdentifier, null);
    }
}
