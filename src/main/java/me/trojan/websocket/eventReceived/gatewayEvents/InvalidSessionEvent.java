package me.trojan.websocket.eventReceived.gatewayEvents;

import com.google.gson.JsonObject;
import me.trojan.websocket.connection.ResumeConnectionHandler;
import me.trojan.websocket.eventReceived.DiscordGatewayEvent;

public class InvalidSessionEvent implements DiscordGatewayEvent {
    private final JsonObject jsonObj;

    public InvalidSessionEvent(JsonObject jsonObj) {
        this.jsonObj = jsonObj;
    }

    @Override
    public void handleEvent() {
        if(jsonObj.has("d")) {
            String d = jsonObj.get("d").getAsString();
            if(d.equals("true")) {
                ResumeConnectionHandler.resume();
            }
            if(d.equals("false")) {
                ResumeConnectionHandler.resumeWithIdentifyPayload();
            }
        }
    }
}
