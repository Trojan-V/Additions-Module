package me.trojan.module.actions.string;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;

public class Char extends BaseScriptAction {

    public Char() {
        super("char");
    }

    /*
     @Syntax with 1 parameter
     [&outvar] = char(<charInt>)
     Example: &var = char(167)
     ------------------------------------------
     @Syntax with 2 parameters
     [&outvar] = char([&outvar],<charInt>)
     Example: &var = char(&var,167)
    */

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(params.length <= 0)
            return null;

        String unicodeCharacter = "Unknown Character";
        if(params.length == 1) {
            unicodeCharacter = new UnicodeConverter(Integer.parseInt(provider.expand(macro, params[0], false))).getUnicodeChar();
        }
        if(params.length > 1) {
            unicodeCharacter = new UnicodeConverter(Integer.parseInt(provider.expand(macro, params[1], false))).getUnicodeChar();
            provider.setVariable(macro, params[0], unicodeCharacter);
        }
        return new ReturnValue(unicodeCharacter);
    }

    static class UnicodeConverter {
        private final int charInt;
        UnicodeConverter(int charInt) {
            this.charInt = charInt;
        }

        protected String getUnicodeChar() {
            return "" + (char) charInt;
        }
    }
}
