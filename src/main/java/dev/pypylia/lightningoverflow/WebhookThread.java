package dev.pypylia.lightningoverflow;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

public class WebhookThread implements Runnable {
    private String postData;

    public static void main(MinecraftClient client, String type, double X, double Y, double Z) {
        if (!LightningOverflowMod.CONFIG.ENABLED ||
            !LightningOverflowMod.CONFIG.isValidURL() ||
            client.player == null ||
            client.isInSingleplayer()
        )
            return;

        if (!LightningOverflowMod.CONFIG.WHITELISTED_SERVERS.contains(
                client.getCurrentServerEntry().address
            ) || MathHelper.magnitude(
                X - client.player.getX(),
                Y - client.player.getY(),
                Z - client.player.getZ()
            ) < LightningOverflowMod.CONFIG.MIN_DISTANCE) return;

        String author = LightningOverflowMod.CONFIG.ANONYMIZE_USERNAME ?
            "\"username\":\"Anonymous Player\",\"avatar_url\":\"https://minotar.net/avatar/MHF_Steve\","
            : String.format(
                "\"username\":\"%s\",\"avatar_url\":\"https://minotar.net/avatar/%s?randomuuid=%s\",",
                client.getSession().getUsername(),
                client.player.getUuid().toString().replace("-", ""),
                UUID.randomUUID().toString().replace("-", "")
            );

        String coords = LightningOverflowMod.CONFIG.LINE_ONLY ? "" :
            String.format("Position: %.1f %.1f %.1f\\n", X, Y, Z);

        double slope = (Z - client.player.getZ()) / (X - client.player.getX());

        WebhookThread runnable = new WebhookThread(String.format(
            "{\"embeds\":[{\"title\":\"%s\",\"description\":\"```%s   Z-int: %.4f\\n   Slope: %.4f```\",%s\"footer\":{\"text\":\"%s\"}}]}",
            type,
            coords,
            Y - slope * Z,
            slope,
            author,
            client.getNetworkHandler()
                .getPlayerList()
                .stream()
                .map(entry -> entry.getProfile().getName())
                .collect(Collectors.joining(", "))
        ));

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public WebhookThread(String postData) {
        this.postData = postData;
    }

    public void run() {
        try {
            URL url = new URL(LightningOverflowMod.CONFIG.WEBHOOK_URL);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json"); 
            conn.setRequestProperty("charset", "utf-8");
            conn.setUseCaches(false);

            OutputStream out = conn.getOutputStream();
            //InputStream inp = conn.getInputStream();
            out.write(this.postData.getBytes(StandardCharsets.UTF_8));

            if (Math.floor(conn.getResponseCode() / 100) != 2) {
                LightningOverflowMod.LOGGER.error(
                    String.format("Non 2XX response code '%d %s'", conn.getResponseCode(), conn.getResponseMessage())
                );
            }

            conn.disconnect();
        } catch (IOException e) {
            LightningOverflowMod.LOGGER.error("Error sending webhook request");
            e.printStackTrace();
            return;
        }
    }
}
