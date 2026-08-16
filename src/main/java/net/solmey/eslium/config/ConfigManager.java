package net.solmey.eslium.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.solmey.eslium.Eslium;
import net.solmey.eslium.config.predictions.Crystal;
import net.solmey.eslium.config.predictions.Minecart;

public class ConfigManager {

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    private static Config config;

    public static void initializeConfig() {
        loadConfig();
    }

    public static Config getConfig() {
        return config;
    }

    private static void loadConfig() {
        Path file = Eslium.getConfigDir().toPath().resolve("eslium.json");

        try {
            Files.createDirectories(file.getParent());

            if (Files.exists(file)) {
                try (var reader = Files.newBufferedReader(file)) {
                    config = GSON.fromJson(reader, Config.class);
                }
            }
            else {
                config = new Config();
            }
        } catch (IOException e) {
            e.printStackTrace();
            config = new Config();
        }
        saveConfig(config);
    }

    public static void saveConfig(Config config) {
        Path file = Eslium.getConfigDir().toPath().resolve("eslium.json");

        try (var writer = Files.newBufferedWriter(file)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
