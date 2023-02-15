package me.trojan.module.iterators;

import me.trojan.base.BaseIterator;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.IScriptedIterator;
import net.minecraft.scoreboard.ScorePlayerTeam;

public class Teams extends BaseIterator {
    public Teams() {
        super(null, null);
    }

    public Teams(IScriptActionProvider provider, IMacro macro) {
        super(provider, macro);

        for (ScorePlayerTeam team : mc.world.getScoreboard().getTeams()) {
            begin();
            add("TEAMPREFIX", team.getPrefix());
            add("TEAMSUFFIX", team.getSuffix());
            add("TEAMDISPLAYNAME", team.getDisplayName());
            add("TEAMNAME", team.getName());
            add("TEAMALLOWFRIENDLYFIRE", team.getAllowFriendlyFire());
            add("TEAMCOLLUSIONRULE", team.getCollisionRule().name());
            add("TEAMCOLOR", team.getColor() != null ? team.getColor().getFriendlyName() : "No TEAMCOLOR");
            add("TEAMDEATHMESSAGEVISIBILITY", team.getDeathMessageVisibility().name());
            add("TEAMFRIENDLYFLAGS", team.getFriendlyFlags());
            add("TEAMNAMETAGVISIBILITY", team.getNameTagVisibility().name());
            add("TEAMSEEFRIENDLYINVISIBLESENABLED", team.getSeeFriendlyInvisiblesEnabled());
            add("TEAMMEMBERS", team.getMembershipCollection());
            end();
        }
    }

    @Override
    public String getIteratorName() {
        return "teams";
    }

    @Override
    public Class<? extends IScriptedIterator> getIteratorClass() {
        return getClass();
    }
}
