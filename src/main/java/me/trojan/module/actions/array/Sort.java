package me.trojan.module.actions.array;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Sort extends BaseScriptAction {
    public Sort() {
        super("sort");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(params.length == 0)
            return new ReturnValue(false);

        String array = provider.expand(macro, params[0], false);
        int arraySize = provider.getArraySize(macro, array);

        ArrayList<Object> list = new ArrayList<>();
        for(int i = 0; i < arraySize; i++) {
            list.add(provider.getArrayElement(macro, array, i));
        }

        Object[] listToArray = list.toArray();
        Arrays.sort(listToArray);

        provider.clearArray(macro, array);
        for(Object o : listToArray) {
            provider.pushValueToArray(macro, array, String.valueOf(o));
        }
        return new ReturnValue(true);
    }
}
