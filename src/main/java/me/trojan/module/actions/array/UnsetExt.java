package me.trojan.module.actions.array;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnsetExt extends BaseScriptAction {
    /**
     * Group 1: The array name with prefix (if any prefix has been provided).
     * Group 2: The index which should be unset within the array.
     */
    private static final Pattern ARRAY_WITH_INDEX_PATTERN = Pattern.compile("^([#&]?[a-z0-9_]+)\\[(\\d*)]$");

    public UnsetExt() {
        super("unsetExt");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(params.length == 0)
            return new ReturnValue(false);

        String arrayNameWithIndex = provider.expand(macro, params[0], false);
        Matcher m = ARRAY_WITH_INDEX_PATTERN.matcher(arrayNameWithIndex);

        /* If matcher doesn't match or provided index is less than 0, return null. */
        if(!m.matches() || (m.matches() && Integer.parseInt(m.group(2)) < 0))
            return new ReturnValue(false);

        String arrayName = m.group(1);
        int arrayIndexToUnset = Integer.parseInt(m.group(2));
        int arraySize = provider.getArraySize(macro, arrayName);

        List<Object> arrayList = new ArrayList<>();
        for(int i = 0; i < arraySize; i++) {
            Object arrayElement = provider.getArrayElement(macro, arrayName, i);
            arrayList.add(arrayElement);
        }

        provider.clearArray(macro, arrayName);
        for(int i = 0; i < arrayList.size(); i++) {
            if(i != arrayIndexToUnset)
                provider.putValueToArray(macro, arrayName, String.valueOf(arrayList.get(i)));
        }
        return new ReturnValue(true);
    }
}
