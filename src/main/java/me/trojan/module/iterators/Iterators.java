package me.trojan.module.iterators;

import me.trojan.Cache;
import me.trojan.base.BaseIterator;
import me.trojan.helpers.ReflectionHelper;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.IScriptedIterator;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptCore;

import java.util.Map;

public class Iterators extends BaseIterator {
    public Iterators() {
        super(null, null);
    }

    public Iterators(IScriptActionProvider provider, IMacro macro) {
        super(provider, macro);

        try {
            if(Cache.iterators == null)
                Cache.iterators = ReflectionHelper.accessPrivateField(ScriptCore.class, "iterators");
            Object iteratorsObject = Cache.iterators.get(ScriptContext.MAIN.getCore());
            @SuppressWarnings("unchecked")
            Map<String, Class<? extends IScriptedIterator>> iteratorsObjectAsMap = (Map<String, Class<? extends IScriptedIterator>>) iteratorsObject;

            for (String iteratorName : iteratorsObjectAsMap.keySet()) {
                begin();
                add("ITERATORNAME", iteratorName);
                end();
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getIteratorName() {
        return "iterators";
    }

    @Override
    public Class<? extends IScriptedIterator> getIteratorClass() {
        return getClass();
    }
}
