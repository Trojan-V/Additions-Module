package me.trojan.base;

import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BaseEventProvider implements IMacroEventProvider, IMacroEventVariableProvider {
    protected final Minecraft mc = Minecraft.getMinecraft();

    /* MacroEventDefinition is basically just the name of the event, and optionally, the permission group. */
    public IMacroEventDefinition getMacroEventDefinition() {
        return new IMacroEventDefinition() {
            @Override
            public String getName() {
                return getEventName();
            }

            @Override
            public String getPermissionGroup() {
                return null;
            }
        };
    }

    @Nonnull
    public abstract String getEventName();


    /* Used to setup instance variables, i.e. %CHAT%, %CHATCLEAN%, etc... */
    @Override
    public abstract void initInstance(String[] instanceVariables);

    /*
     Common return:
     return help;
    */
    @Override
    public abstract List<String> getHelp(IMacroEvent macroEvent);


    /* getVariable and getVariables methods are required if any instanceVariables should be able to be used in the event. */
    public Object getVariable(String variableName) {
        return getInstanceVarsMap().get(variableName);
    }

    public Set<String> getVariables() {
        return getInstanceVarsMap().keySet();
    }

    public abstract Map<String, Object> getInstanceVarsMap();


    @Override
    public void onInit() {
        for (ScriptContext context : ScriptContext.getAvailableContexts()) {
            context.getCore().registerEventProvider(this);
        }
    }

    /* If the IMacroEventDispatcher interface is implemented, the method has to return "this" instead of null. */
    @Override
    public IMacroEventDispatcher getDispatcher() {
        return null;
    }

    @Override
    public void updateVariables(boolean clock) {

    }

}
