package me.trojan.module.actions.array;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;

import java.util.ArrayList;
import java.util.Collections;

public class Shuffle extends BaseScriptAction {
    public Shuffle() {
        super("shuffle");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(params.length == 0)
            return new ReturnValue(false);

        String arrayName = provider.expand(macro, params[0], false);
        int arraySize = provider.getArraySize(macro, arrayName);
        ArrayList<Object> list = new ArrayList<>();
        for(int i = 0; i < arraySize; i++) {
            list.add(provider.getArrayElement(macro, arrayName, i));
        }
        Collections.shuffle(list);
        provider.clearArray(macro, arrayName);
        for(Object o : list) {
            provider.pushValueToArray(macro, arrayName, String.valueOf(o));
        }
        return new ReturnValue(true);
    }
}
