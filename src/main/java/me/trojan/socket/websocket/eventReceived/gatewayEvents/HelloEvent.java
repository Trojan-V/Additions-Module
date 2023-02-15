package me.trojan.socket.websocket.eventReceived.gatewayEvents;

import com.google.gson.JsonObject;
import me.trojan.socket.websocket.Config;
import me.trojan.socket.websocket.Heartbeat;
import me.trojan.socket.websocket.WebSocket;
import me.trojan.socket.websocket.eventReceived.DiscordGatewayEvent;
import me.trojan.socket.websocket.payload.IdentificationPayload;

public class HelloEvent implements DiscordGatewayEvent {
    private final JsonObject jsonObj;

    public HelloEvent(JsonObject jsonObj) {
        this.jsonObj = jsonObj;
    }

    @Override
    public void handleEvent() {
        JsonObject dObj = jsonObj.get("d").getAsJsonObject();
        int heartbeatInterval = dObj.get("heartbeat_interval").getAsInt();

        WebSocket.heartbeatInstance = new Heartbeat(heartbeatInterval);

        String identifyPayload = new IdentificationPayload(Config.USER_TOKEN).getPayloadString();
        WebSocket.ws.send(identifyPayload);
    }
}
