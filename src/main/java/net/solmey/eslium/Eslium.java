package net.solmey.eslium;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.solmey.eslium.config.ConfigManager;

public class Eslium {

    public static File configDir;

    public static void commonInit() {
        ensureDirectoriesReady();
        ConfigManager.initializeConfig();
    }

    private static void ensureDirectoriesReady() {
        if (configDir == null) {
            File serverRoot = new File(System.getProperty("user.dir"));

            configDir = new File(serverRoot, "config");
        }

        if (!configDir.exists()) {
            configDir.mkdirs();
        }
    }

    public static File getConfigDir() {
        return configDir;
    }

    public static boolean shouldWork() {
        Minecraft mc = Minecraft.getInstance();
        if (
            mc.level == null ||
            mc.player == null ||
            mc.isLocalServer() ||
            mc.getConnection() == null
        ) {
            return false;
        }
        if (ConfigManager.getConfig().enabled == false) return false;

        return true;
    }
}
