package me.trojan.socket.websocket.connection;

import com.google.gson.JsonObject;
import me.trojan.socket.websocket.Cache;
import me.trojan.socket.websocket.Config;
import me.trojan.socket.websocket.WebSocket;
import me.trojan.socket.websocket.payload.IdentificationPayload;

import java.net.URI;
import java.net.URISyntaxException;

public class ResumeConnectionHandler {
    public static void resume() {
        WebSocket oldWebsocket = WebSocket.getInstance();
        oldWebsocket.close();

        try {
            WebSocket newWebsocket = new WebSocket(new URI(Cache.resumeGatewayURL));
            newWebsocket.connect();
            WebSocket.ws = newWebsocket;
            WebSocket.ws.send(getResumePayload());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void resumeWithIdentifyPayload() {
        WebSocket oldWebsocket = WebSocket.getInstance();
        oldWebsocket.close();

        try {
            WebSocket newWebsocket = new WebSocket(new URI(Cache.resumeGatewayURL));
            newWebsocket.connect();
            newWebsocket.send(new IdentificationPayload(Config.USER_TOKEN).getPayloadString());
            WebSocket.ws = newWebsocket;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getResumePayload() {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("op", 6);

        JsonObject dObj = new JsonObject();
        dObj.addProperty("token", Config.USER_TOKEN);
        dObj.addProperty("session_id", Cache.sessionID);
        dObj.addProperty("seq", Cache.lastSequence);
        jsonObj.add("d", dObj);
        return jsonObj.getAsString();
    }
}
