package me.trojan.socket.websocket.actionsToPerform;

import net.eq2online.macros.core.Macros;
import net.eq2online.macros.scripting.api.IMacroTemplate;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionHandler {
    private final String actionToPerform;
    private final Minecraft mc = Minecraft.getMinecraft();

    public ActionHandler(String actionToPerform) {
        this.actionToPerform = actionToPerform;
    }

    public void handleAction() {
        if(actionToPerform.startsWith("connect ")) {
            Pattern p = Pattern.compile("^connect ([\\w.-_]+)");
            Matcher m = p.matcher(actionToPerform);
            String serverIP;
            if(m.find()) {
                serverIP = m.group(1);
                ServerData serverData = new ServerData("", serverIP, false);
                connectToServer(serverData);
            }
            return;
        }
        if(actionToPerform.equalsIgnoreCase("disconnect")) {
            disconnectFromServer();
            return;
        }
        if(actionToPerform.equalsIgnoreCase("terminate")) {
            mc.shutdown();
            return;
        }


        Random ran = new Random();
        IMacroTemplate tpl = Macros.getInstance().createFloatingTemplate("$${ " + actionToPerform + "}$$", "WS-Code" + ran.nextInt());
        Macros.getInstance().playMacro(tpl, false, ScriptContext.MAIN, null);
    }

    public void connectToServer(ServerData serverData) {
        if(mc.currentScreen == null)
            return;
        mc.addScheduledTask( () -> mc.displayGuiScreen(new GuiMultiplayer(mc.currentScreen)) );
        mc.addScheduledTask( () -> mc.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), mc, serverData)) );
    }

    public void disconnectFromServer() {
        String reason = "Force Disconnect";
        ITextComponent comp = new TextComponentString("Forced.");
        mc.addScheduledTask( () -> mc.displayGuiScreen(new GuiDisconnected(new GuiMultiplayer(new GuiMainMenu()), reason, comp)) );
        mc.addScheduledTask( () -> mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu())) );
        mc.addScheduledTask( () -> mc.displayGuiScreen(new GuiMainMenu()));
    }
}
