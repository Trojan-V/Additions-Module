package me.trojan.module.iterators;

import me.trojan.base.BaseIterator;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.IScriptedIterator;
import net.minecraft.client.network.NetworkPlayerInfo;

public class Tablist extends BaseIterator {
    public Tablist() {
        super(null, null);
    }

    public Tablist(IScriptActionProvider provider, IMacro macro) {
        super(provider, macro);

        for (NetworkPlayerInfo info : mc.player.connection.getPlayerInfoMap()) {
            String name = mc.ingameGUI.getTabList().getPlayerName(info);
            begin();
            add("TABPLAYERNAME", name);
            end();
        }
    }

    @Override
    public String getIteratorName() {
        return "tablist";
    }

    @Override
    public Class<? extends IScriptedIterator> getIteratorClass() {
        return getClass();
    }
}
