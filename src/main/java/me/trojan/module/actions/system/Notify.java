package me.trojan.module.actions.system;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class Notify extends BaseScriptAction {

    public Notify() {
        super("notify");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        String title = (params.length > 0) ? provider.expand(macro, params[0], false) : null;
        String message = (params.length > 1) ? provider.expand(macro, params[1], false) : null;

        try {
            SystemTrayUtils.displaySystemTray(title, message);
            return new ReturnValue(true);
        } catch (MalformedURLException | AWTException e) {
            e.printStackTrace();
        }
        return new ReturnValue(false);
    }


    static class SystemTrayUtils {
        private static TrayIcon trayIcon;
        protected static void displaySystemTray(String title, String message) throws MalformedURLException, AWTException {
            if(!SystemTray.isSupported()) return;
            if(trayIcon == null) {
                setDefaultTrayIcon();
            }
            trayIcon.displayMessage((title != null) ? title : "Notifications", (message != null) ? message : "", TrayIcon.MessageType.INFO);
        }

        private static void setDefaultTrayIcon() throws MalformedURLException, AWTException {
            SystemTray tray = SystemTray.getSystemTray();
            URL url = new URL("https://image.prntscr.com/image/2Ve0U2TcRQyqcSwJeY3-vw.png");
            Image image = Toolkit.getDefaultToolkit().getImage(url);
            trayIcon = new TrayIcon(image);
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("Notifications");
            tray.add(trayIcon);
        }
    }

}

