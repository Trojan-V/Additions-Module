package me.trojan.socket.websocket;

public class Config {
    public static final String WEBSOCKET_URL = "wss://gateway.discord.gg/?v=10&encoding=json";
    public static final String USER_TOKEN = System.getenv("DISCORD_USER_TOKEN");
}
