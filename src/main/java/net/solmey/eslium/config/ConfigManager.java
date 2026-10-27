package net.solmey.eslium.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.solmey.eslium.Eslium;

public class ConfigManager {

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    private static Config config;

    private static File configDir;
    private static Path file;


    public static void initializeConfig() {
        if (configDir == null) {
            File serverRoot = new File(System.getProperty("user.dir"));

            configDir = new File(serverRoot, "config");
        }

        file = configDir.toPath().resolve(Eslium.MOD_ID + ".json");

        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        loadConfig();
    }

    public static Config getConfig() {
        return config;
    }

    private static void loadConfig() {
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
        try (var writer = Files.newBufferedWriter(file)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
