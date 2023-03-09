package me.trojan.websocket;

public class Opcodes {
    public static final int DISPATCH = 0;
    public static final int HEARTBEAT = 1;
    public static final int IDENTIFY = 2;

    public static final int RESUME = 6;
    public static final int RECONNECT = 7;

    public static final int INVALID_SESSION = 9;
    public static final int HELLO = 10;
    public static final int HEARTBEAT_ACK = 11;
}
