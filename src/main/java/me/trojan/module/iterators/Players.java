package me.trojan.module.iterators;

import me.trojan.base.BaseIterator;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.IScriptedIterator;
import net.minecraft.client.network.NetworkPlayerInfo;

public class Players extends BaseIterator {
    public Players() {
        super(null, null);
    }

    public Players(IScriptActionProvider provider, IMacro macro) {
        super(provider, macro);

        for (NetworkPlayerInfo playerEntry : mc.player.connection.getPlayerInfoMap()) {
            begin();
            add("PLAYERNAME", playerEntry.getGameProfile().getName());
            add("PLAYERUUID", playerEntry.getGameProfile().getId().toString());
            add("ISLEGACY", playerEntry.getGameProfile().isLegacy());
            add("PLAYERDISPLAYNAME", (playerEntry.getDisplayName() != null) ? playerEntry.getDisplayName().getFormattedText() : playerEntry.getGameProfile().getName());
            end();
        }
    }

    @Override
    public String getIteratorName() {
        return "players";
    }

    @Override
    public Class<? extends IScriptedIterator> getIteratorClass() {
        return getClass();
    }
}
