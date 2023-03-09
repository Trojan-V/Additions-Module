package me.trojan.websocket.eventReceived.gatewayEvents;

import com.google.gson.JsonObject;
import me.trojan.websocket.Config;
import me.trojan.websocket.Heartbeat;
import me.trojan.websocket.WebSocket;
import me.trojan.websocket.eventReceived.DiscordGatewayEvent;
import me.trojan.websocket.payload.IdentificationPayload;

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

        String identifyPayload = new IdentificationPayload(Config.BOT_TOKEN).getPayloadString();
        WebSocket.ws.send(identifyPayload);
    }
}
