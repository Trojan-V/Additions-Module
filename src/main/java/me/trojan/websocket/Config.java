package me.trojan.websocket;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public static final String WEBSOCKET_URL = "wss://gateway.discord.gg/?v=10&encoding=json";
    public static final String BOT_TOKEN = System.getenv("DISCORD_BOT_TOKEN");

    public static final List<String> LISTENING_CHANNEL_IDS = new ArrayList<>();
    public static final List<String> WHITELISTED_USERNAMES = new ArrayList<>();

    static {
        WHITELISTED_USERNAMES.add("MC - Remote Control");
        WHITELISTED_USERNAMES.add("Trojan");

        LISTENING_CHANNEL_IDS.add("1074381633899208744");
    }
}
