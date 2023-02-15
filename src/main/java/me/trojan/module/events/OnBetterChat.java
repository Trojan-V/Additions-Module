package me.trojan.module.events;

import com.mumfrey.liteloader.ChatListener;
import me.trojan.base.BaseEventProvider;
import me.trojan.helpers.MacroModRegistryHelper;
import net.eq2online.macros.scripting.api.IMacroEvent;
import net.eq2online.macros.scripting.api.IMacroEventManager;
import net.eq2online.util.Util;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.*;

public class OnBetterChat extends BaseEventProvider implements ChatListener {
    private IMacroEvent onBetterChat;
    private static IMacroEventManager manager = null;
    private final Map<String, Object> vars = new HashMap<>();
    private static final List<String> help;

    /* Set the contents of the helpList which will be shown ingame while having the GUI of the event open. */
    static {
        List<String> helpList = new ArrayList<>();
        helpList.add(Util.convertAmpCodes("&7Hello World."));
        help = Collections.unmodifiableList(helpList);
    }

    @Override
    public @Nonnull String getEventName() {
        return "onBetterChat";
    }

    @Override
    public void registerEvents(IMacroEventManager manager) {
        OnBetterChat.manager = manager;
        this.onBetterChat = manager.registerEvent(this, getMacroEventDefinition());
        this.onBetterChat.setVariableProviderClass(getClass());
    }

    @Override
    public void initInstance(String[] instanceVariables) {
        vars.put("CHAT", instanceVariables[0]);
        vars.put("CHATCLEAN", StringUtils.stripControlCodes(instanceVariables[0]));
        vars.put("CHATJSON", instanceVariables[1]);
    }


    @Override
    public void onChat(ITextComponent chat, String message) {
        String json = ITextComponent.Serializer.componentToJson(chat);
        manager.sendEvent(this.onBetterChat, message, json);
    }

    @Override
    public void onInit() {
        try {
            MacroModRegistryHelper.registerChatListener(this);
            super.onInit();
        } catch(NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            e.printStackTrace();
        }
    }

    /*
     BOTH CONSTRUCTORS REQUIRED FOR THE EVENT TO WORK.
     Otherwise, the instance variables are not working correctly and just return CHATJSON, CHAT and CHATCLEAN.
    */
    public OnBetterChat() {}
    public OnBetterChat(IMacroEvent e) {}



    /* Force implementation, used internally by MacroMod to get and process certain data. */
    @Nonnull
    @Override
    public List<String> getHelp(IMacroEvent macroEvent) {
        return help;
    }


    @Override
    public Map<String, Object> getInstanceVarsMap() {
        return this.vars;
    }


    /* Force implementation, not used for any logic processed by the event. */
    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public void init(File configPath) {}

    @Override
    public void upgradeSettings(String version, File configPath, File oldConfigPath) {}

    @Override
    public String getName() {
        return null;
    }

}
