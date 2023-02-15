package me.trojan.module.iterators;

import me.trojan.base.BaseIterator;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.IScriptedIterator;

public class EnvironmentalVariables extends BaseIterator {
    public EnvironmentalVariables() {
        super(null, null);
    }

    public EnvironmentalVariables(IScriptActionProvider provider, IMacro macro) {
        super(provider, macro);

        for (String envVar : provider.getEnvironmentVariables()) {
            begin();
            add("NAME", envVar);
            end();
        }
    }

    @Override
    public String getIteratorName() {
        return "envvars";
    }

    @Override
    public Class<? extends IScriptedIterator> getIteratorClass() {
        return getClass();
    }
}
