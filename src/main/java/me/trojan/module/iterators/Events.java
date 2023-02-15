package me.trojan.module.iterators;

import me.trojan.base.BaseIterator;
import net.eq2online.macros.core.Macros;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroEvent;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.IScriptedIterator;

public class Events extends BaseIterator {
    public Events() {
        super(null, null);
    }

    public Events(IScriptActionProvider provider, IMacro macro) {
        super(provider, macro);

        for (IMacroEvent event : Macros.getInstance().getEventManager().getEvents()) {
            begin();
            add("EVENTNAME", event.getName());
            add("EVENTICONPATH", event.getIcon().getIconName());
            end();
        }
    }

    @Override
    public String getIteratorName() {
        return "events";
    }

    @Override
    public Class<? extends IScriptedIterator> getIteratorClass() {
        return getClass();
    }
}
