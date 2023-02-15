package me.trojan.internal;

import com.mumfrey.liteloader.util.log.LiteLoaderLogger;
import me.trojan.ModuleInfo;
import me.trojan.base.BaseScriptAction;
import me.trojan.engine.EditedVariableProviderShared;
import me.trojan.helpers.ReflectionHelper;
import me.trojan.module.parsers.*;
import me.trojan.socket.websocket.Config;
import me.trojan.socket.websocket.WebSocket;
import net.eq2online.macros.core.Macros;
import net.eq2online.macros.scripting.ActionParser;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.*;
import net.eq2online.macros.scripting.variable.VariableManager;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@APIVersion(ModuleInfo.API_VERSION)
public class ScriptActionInternalModuleInitialization extends BaseScriptAction {
    private final List<Class<? extends ActionParser>> actionParsersToRemove = new ArrayList<>();
    private final List<ActionParser> actionParsersToAdd = new ArrayList<>();

    public ScriptActionInternalModuleInitialization() {
        super("internalModuleInitialization");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        return null;
    }

    @Override
    public void onInit() {
        /* Register Action Parsers added by this module, located in me.trojan.module.parsers. */
        handleActionParserRegistration();
        replaceVariableProviderShared();
        addDiscordGatewayListener();
        initializeModule();
    }

    private void addDiscordGatewayListener() {
        try {
            WebSocket.ws = new WebSocket(new URI(Config.WEBSOCKET_URL));
            WebSocket.ws.connect();
            LiteLoaderLogger.info("Successfully created new WebSocket object pointing towards: " + Config.WEBSOCKET_URL);
        } catch (URISyntaxException e) {
            LiteLoaderLogger.warning("Couldn't establish a connection with the Discord Gateway API.");
        }
    }

    private void handleActionParserRegistration() {
        removeActionParser(ActionParserAssignment.class);
        removeActionParser(ActionParserDirective.class);
        removeActionParser(ActionParserAction.class);
        addActionParser(new EditedActionParserAssignment(ScriptContext.MAIN));
        addActionParser(new EditedActionParserAction(ScriptContext.MAIN));
        addActionParser(new EditedActionParserDirective(ScriptContext.MAIN));
        addActionParser(new ActionParserInc(ScriptContext.MAIN));
        addActionParser(new ActionParserDec(ScriptContext.MAIN));
    }

    private void replaceVariableProviderShared() {
        IScriptActionProvider provider = ScriptContext.MAIN.getScriptActionProvider();
        IVariableProviderShared defaultVariableProviderShared = provider.getSharedVariableProvider();
        provider.unregisterVariableProvider(defaultVariableProviderShared);
        EditedVariableProviderShared editedVariableProviderShared = new EditedVariableProviderShared(this.macros);
        provider.registerVariableProvider(editedVariableProviderShared);
        try {
            Field sharedVariableProvider = ReflectionHelper.accessPrivateField(VariableManager.class, "sharedVariableProvider");
            sharedVariableProvider.set(provider, editedVariableProviderShared);
            sharedVariableProvider.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /* This method should never be called more than once, and should never be called by yourself.
     * It's called from the class ScriptActionInternalModuleInitialization from the internal package and is handling unregistering and registering of all classes which require this.
     * Examples are ActionParsers, but also Threads which handle certain logic, such as DiscordListeners will be started through this method. */
    private void initializeModule() {
        removeActionParsers();
        reAddActionParsers();
        //DiscordCommandListener.INSTANCE.start();
        // Instantiate a new thread of the Main Websockets Thread which will listen to Discord Gateway API and get messages if any have been sent.
        new BetterModuleLoader(Macros.getInstance().getMacrosDirectory()).loadModules(null);
        //new WebSocketConnectionThread().start();
        //Log.info("ws connection thread started.");
    }

    private void removeActionParsers() {
        if(actionParsersToRemove.isEmpty())
            return;

        IScriptParser scriptParser = ScriptContext.MAIN.getParser();
        Object parsers = null;
        Field parsersField = null;
        try {
            parsersField = ReflectionHelper.accessPrivateField(ScriptParser.class, "parsers");
            parsers = parsersField.get(scriptParser);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if(parsers == null)
            return;

        /* The list stored in the ScriptParser class which holds all registered actions parsers.
         This list is being modified to remove already registered actions parsers. */
        @SuppressWarnings("unchecked")
        List<ActionParser> registeredParsers = (List<ActionParser>) parsers;
        for(int i = 0; i < registeredParsers.size(); i++) {
            Class<? extends ActionParser> clazz = registeredParsers.get(i).getClass();
            boolean actionParserShouldBeRemoved = actionParsersToRemove.contains(clazz);
            if(actionParserShouldBeRemoved)
                registeredParsers.remove(i);
        }
        parsersField.setAccessible(false);
    }

    private void reAddActionParsers() {
        for (ActionParser parser : actionParsersToAdd) {
            for (ScriptContext context : ScriptContext.getAvailableContexts()) {
                context.getParser().addActionParser(parser);
            }
        }
    }

    private void addActionParser(ActionParser parser) {
        actionParsersToAdd.add(parser);
    }

    /**
     * @param actionParserClass The class of the ActionParser to remove, i.e. ActionParserDirective.class
     */
    private void removeActionParser(Class<? extends ActionParser> actionParserClass) {
        actionParsersToRemove.add(actionParserClass);
    }

}
