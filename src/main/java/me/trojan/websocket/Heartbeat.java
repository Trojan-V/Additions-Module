package me.trojan.websocket;

import com.google.gson.JsonObject;

import java.util.Timer;
import java.util.TimerTask;

public class Heartbeat {
    Timer timer;

    public Heartbeat(int heartbeatInterval) {
        double jitter = Math.random();
        timer = new Timer();
        timer.schedule(new RemindTask(), (long) (heartbeatInterval * jitter), heartbeatInterval);
    }

    class RemindTask extends TimerTask {
        public void run() {
            System.out.println("Sending heartbeatPayload: " + getHeartbeatPayload());
            sendHeartbeat();
        }
    }

    public void sendHeartbeat() {
        WebSocket.getInstance().send(getHeartbeatPayload());
    }

    private String getHeartbeatPayload() {
        JsonObject heartbeatJson = new JsonObject();
        heartbeatJson.addProperty("op", 1);
        heartbeatJson.add("d", null);
        if(!(Cache.lastSequence == -1)) {
            heartbeatJson.addProperty("s", Cache.lastSequence);
        }
        return heartbeatJson.toString();
    }
}
