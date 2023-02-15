package me.trojan.socket.websocket;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.trojan.socket.websocket.connection.ResumeConnectionHandler;
import me.trojan.socket.websocket.eventReceived.gatewayEvents.DispatchEvent;
import me.trojan.socket.websocket.eventReceived.gatewayEvents.HelloEvent;
import me.trojan.socket.websocket.eventReceived.gatewayEvents.InvalidSessionEvent;
import me.trojan.socket.wsclient.client.WebSocketClient;
import me.trojan.socket.wsclient.handshake.ServerHandshake;

import java.net.URI;

public class WebSocket extends WebSocketClient {
    public static WebSocket ws;
    public static Heartbeat heartbeatInstance;

    public WebSocket(URI serverUri) {
        super(serverUri);
    }

    public static WebSocket getInstance() {
        return ws;
    }

    public static Heartbeat getHeartbeatInstance() {
        return heartbeatInstance;
    }

    @Override
    public void onOpen(ServerHandshake handShakeData) {
        System.out.println("Connection has been opened up.");
    }

    @Override
    public void onMessage(String message) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(message);
        JsonObject jsonObj = (JsonObject) jsonElement;
        if(!jsonObj.has("op")) {
            return;
        }

        int opCode = jsonObj.get("op").getAsInt();
        switch (opCode) {
            case Opcodes.DISPATCH:
                DispatchEvent dispatchEvent = new DispatchEvent(jsonObj);
                dispatchEvent.handleEvent();
                break;
            case Opcodes.HEARTBEAT:
                System.out.println("Additional heartbeat received from Websocket, immediately sending heartbeat. (received message: " + message + ")");
                getHeartbeatInstance().sendHeartbeat();
                break;
            case Opcodes.HELLO:
                HelloEvent helloEvent = new HelloEvent(jsonObj);
                helloEvent.handleEvent();
                break;
            case Opcodes.RECONNECT:
                ResumeConnectionHandler.resume();
            case Opcodes.INVALID_SESSION:
                InvalidSessionEvent invalidSessionEvent = new InvalidSessionEvent(jsonObj);
                invalidSessionEvent.handleEvent();
                break;
            case Opcodes.HEARTBEAT_ACK:
                System.out.println("Heartbeat Acknowledgement received.");
                break;
            default:
                System.out.println("Received opCode which isn't handled by the opCode-parser.");
                System.out.println("unhandled message: " + message);
        }
    }

    // TODO: Resume connection if it has been closed without any close code.
    @Override
    public void onClose(int code, String reason, boolean remote) {
        if(code == 4000 || code == 4001 || code == 4002 || code == 4003 || code == 4005 || code == 4006 || code == 4007 || code == 4008 || code == 4009) {
            ResumeConnectionHandler.resume();
        }
        System.out.println("Connection has been closed for reason: " + reason + " with code: " + code);
    }

    @Override
    public void onError(Exception e) {
        System.out.println("An error occurred with Exception-Message: " + e.getMessage());
    }
}
