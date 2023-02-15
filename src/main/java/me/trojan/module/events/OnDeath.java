package me.trojan.module.events;

import me.trojan.Cache;
import me.trojan.base.BaseEventProvider;
import net.eq2online.macros.scripting.api.IMacroEvent;
import net.eq2online.macros.scripting.api.IMacroEventDispatcher;
import net.eq2online.macros.scripting.api.IMacroEventManager;
import net.eq2online.util.Util;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;
import java.util.*;

public class OnDeath extends BaseEventProvider implements IMacroEventDispatcher {
    private IMacroEvent onDeath;
    private static IMacroEventManager manager = null;
    private final Map<String, Object> vars = new HashMap<>();
    private static final List<String> help;

    static {
        List<String> helpList = new ArrayList<>();
        helpList.add(Util.convertAmpCodes("&donDeath-Event"));
        help = Collections.unmodifiableList(helpList);
    }

    @Override
    public @Nonnull String getEventName() {
        return "onDeath";
    }

    @Override
    public void registerEvents(IMacroEventManager manager) {
        OnDeath.manager = manager;
        onDeath = manager.registerEvent(this, getMacroEventDefinition());
        onDeath.setVariableProviderClass(getClass());
    }

    @Override
    public void onTick(IMacroEventManager manager, Minecraft minecraft) {
        if(mc.player.isDead && !Cache.playerIsDead) {
            OnDeath.manager.sendEvent(onDeath);
            Cache.playerIsDead = true;
        }
    }

    @Override
    public void initInstance(String[] instanceVariables) {}

    @Nonnull
    @Override
    public List<String> getHelp(IMacroEvent macroEvent) {
        return help;
    }

    @Override
    public Map<String, Object> getInstanceVarsMap() {
        return vars;
    }

    public OnDeath() {}

    public OnDeath(IMacroEvent e) {}

    @Override
    public IMacroEventDispatcher getDispatcher() {
        return this;
    }
}
