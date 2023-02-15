package me.trojan.helpers;


import com.mumfrey.liteloader.ChatListener;
import com.mumfrey.liteloader.client.PacketEventsClient;
import com.mumfrey.liteloader.core.PacketEvents;
import net.eq2online.macros.compatibility.Reflection;
import net.eq2online.macros.scripting.ActionParser;
import net.eq2online.macros.scripting.api.IScriptAction;
import net.eq2online.macros.scripting.api.IScriptParser;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.macros.scripting.parser.ScriptParser;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MacroModRegistryHelper {

    /**
     * @param chatListener
     * Usually the keyword 'this' is passed as parameter for chatListener as the class which calls this method has to registered as the class which is listening to the chat.
     * Otherwise this is the class which should be registered, f.ex. MaddoxBatPhone.class
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * These exceptions are thrown when the declared field (packetsEventsField) can't be accessed or is not existing.
     */
    public static void registerChatListener(ChatListener chatListener) throws NoSuchFieldException, IllegalAccessException {
        Field packetEventsField = PacketEvents.class.getDeclaredField("instance");
        packetEventsField.setAccessible(true);
        PacketEventsClient packetEventsClient = (PacketEventsClient) packetEventsField.get(null);
        packetEventsClient.registerChatListener(chatListener);
        packetEventsField.setAccessible(false);
    }

    /*
     Unregisters an already loaded script actions.
     Allowing to register a script actions with an identical actionName after the unregistering process has been finished.

     DISCLAIMER: If no new actions is registered instead of the old one, the old actions will be kept as it has been before.
    */
    public static void unregisterScriptAction(String actionName) {
        // Getting the actionsField and the actionsObject, which represents the current instance of actions which are registered within MacroMod.
        Field actionsField = null;
        Object actionsObject = null;
        try {
            actionsField = getActionsField();
            actionsObject = getActionsObject(actionsField);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        /*
         Since actionsObject has been initialized as null, it might happen that the actionsObject can't be accessed for whatever reason,
         hence why we check here if the actionsObject is actually an instance of Map to validate the try-catch block didn't enter the catch block
         and couldn't retrieve the actionsObject.
        */
        if(!(actionsObject instanceof Map))
            return;

        Map<String, IScriptAction> actions = MacroModRegistryHelper.getActions(actionsObject);
        removeActionFromRegisteredListsAndMaps(actionName.toLowerCase(), actions);
        actionsField.setAccessible(false);

    }

    /*
     Removes the entries for the provided actionName from the LinkedList (actionsList)
     and Map (actions) MacroMod internally uses as indicators which ScriptActions have to be registered.
    */
    private static void removeActionFromRegisteredListsAndMaps(String actionName, Map<String, IScriptAction> actions) {
        LinkedList<IScriptAction> actionsList = MacroModRegistryHelper.getActionsList();
        if (actions.containsKey(actionName)) {
            actions.remove(actionName);
            for (int i = 0; i < actionsList.size(); i++) {
                IScriptAction listEntry = actionsList.get(i);
                if (listEntry.toString().equals(actionName))
                    actionsList.remove(i);
            }
        }
    }


    /*
     Allows access to the @Field private final Map<String, IScriptAction> actions = new HashMap<String, IScriptAction>();
     in the @Class ScriptCore.java (@Package net.eq2online.macros.scripting.parser)
    */
    private static Field getActionsField() throws NoSuchFieldException {
        Field actionsField = ScriptContext.MAIN.getCore().getClass().getDeclaredField("actions");
        if (!actionsField.isAccessible())
            actionsField.setAccessible(true);
        return actionsField;
    }


    /* Returns the current instance of the registered actions. */
    private static Object getActionsObject(Field actionsField) throws IllegalAccessException {
        return actionsField.get(ScriptContext.MAIN.getCore());
    }

    /* Returns an internal list (LinkedList) which contains all actions currently registered in MacroMod,
       can be edited to remove actions from the list and with that from MacroMod.
    */
    private static LinkedList<IScriptAction> getActionsList() {
        return (LinkedList<IScriptAction>)ScriptContext.MAIN.getCore().getActionsList();
    }

    /*
     Returns another internal list (Map) which contains all actions currently registered in MacroMod,
     can be edited to remove actions from the list and with that from MacroMod.
    */
    private static Map<String, IScriptAction> getActions(Object actionsObject) {
        return (Map<String, IScriptAction>)actionsObject;
    }

    public static List<ActionParser> getRegisteredActionParsers() {
        IScriptParser scriptParser = ScriptContext.MAIN.getParser();
        Object parsers = null;
        try {
            parsers = Reflection.getPrivateValue(ScriptParser.class, scriptParser, "parsers");
        } catch (NoSuchFieldException ignored) {}

        List<ActionParser> parserList = (List<ActionParser>) parsers;
        return parserList;
    }
}
