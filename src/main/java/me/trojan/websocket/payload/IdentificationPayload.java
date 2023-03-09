package me.trojan.websocket.payload;

import com.google.gson.JsonObject;
import me.trojan.websocket.intents.GatewayIntentBits;

public class IdentificationPayload {
    private final String discordToken;

    public IdentificationPayload(String discordToken) {
        this.discordToken = discordToken;
    }

    public String getPayloadString() {
        return createPayload().toString();
    }

   private JsonObject createPayload() {
       JsonObject payload = new JsonObject();
       payload.addProperty("op", 2);
       payload.add("d", createDKeyObject());
       return payload;
   }

    private JsonObject createDKeyObject() {
        JsonObject d = new JsonObject();
        d.addProperty("token", this.discordToken);
        d.add("properties", CreatePropertiesKeyObject());
        d.addProperty("intents", getIntentValue());
        return d;
    }

    private int getIntentValue() {
        return GatewayIntentBits.GUILDS.getIntValue() +
                GatewayIntentBits.GUILD_MESSAGES.getIntValue() +
                GatewayIntentBits.MESSAGE_CONTENT.getIntValue();
    }

   private JsonObject CreatePropertiesKeyObject() {
        JsonObject properties = new JsonObject();
        properties.addProperty("os", "windows");
        properties.addProperty("browser", "chrome");
        properties.addProperty("device", "pc");
        return properties;
   }
}
