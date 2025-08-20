package com.jarrowsmith123.network;

import com.google.gson.Gson;
import com.jarrowsmith123.MctuiPlugin;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Request;
import okhttp3.Response;
import org.bukkit.Bukkit;


public class HttpManager {
    private final MctuiPlugin plugin;
    private final Gson gson;
    private final OkHttpClient client;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public HttpManager(MctuiPlugin plugin) {
        this.plugin = plugin;
        this.gson = new Gson();
        this.client = new OkHttpClient();

    }

    public void sendLocationUpdate(PlayerLocation location) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {

            try {
                String jsonPayload = this.gson.toJson(location);

                RequestBody body = RequestBody.create(jsonPayload, JSON);

                Request request = new Request.Builder()
                        .url("http://0.0.0.0:3000/api/location")
                        .post(body)
                        .build();

                try (Response response = this.client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        this.plugin.getLogger().info("Successfully sent event: " + location);
                    } else {
                        assert response.body() != null;
                        this.plugin.getLogger().warning(
                                "Failed to send event. Status code: " + response.code()
                                        + " | Response: " + response.body().string()
                        );
                    }
                }

            } catch (Exception e) {
                this.plugin.getLogger().severe("An error occurred while sending an event: " + e.getMessage());
            }
        });
    }
}
