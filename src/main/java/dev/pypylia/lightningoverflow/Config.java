package dev.pypylia.lightningoverflow;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import net.fabricmc.loader.api.FabricLoader;

public class Config {
    private static final File file = FabricLoader.getInstance().getGameDir().resolve("config")
        .resolve("lightingoverflow.json").toFile();

    private static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .serializeNulls()
        .create();

    public static Config load() {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdir();

                Config defaults = new Config();

                FileWriter writer = new FileWriter(file);
                GSON.toJson(defaults, writer);
                writer.close();

                return defaults;
            }

            Config config = GSON.fromJson(new FileReader(file), Config.class);

            return config;
        } catch (Exception e) {
            LightningOverflowMod.LOGGER.error("An error occurred loading the config file");
            e.printStackTrace();
            return new Config();
        }
    }

    public void save() {
        try {
            FileWriter writer = new FileWriter(file);
            GSON.toJson(this, writer);
            writer.close();
        } catch (Exception e) {
            LightningOverflowMod.LOGGER.error("An error occurred saving the config file");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public boolean isValidURL() {
        try {
            URI uri = new URL(this.WEBHOOK_URL).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SerializedName("enabled")
    public boolean ENABLED = true;

    @SerializedName("anonymizeUsername")
    public boolean ANONYMIZE_USERNAME = false;

    @SerializedName("lineOnly")
    public boolean LINE_ONLY = false;

    @SerializedName("webhookURL")
    public String WEBHOOK_URL = "";

    @SerializedName("minDistance")
    public int MIN_DISTANCE = 0;

    @SerializedName("whitelistedServers")
    public ArrayList<String> WHITELISTED_SERVERS = new ArrayList<String>();
}
