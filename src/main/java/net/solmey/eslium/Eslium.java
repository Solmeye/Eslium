package net.solmey.eslium;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.solmey.eslium.config.ConfigManager;

public class Eslium {

    public static final Logger LOGGER = LogManager.getLogger("eslium");

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
            //mc.player.getInventory() == null ||
            mc.isLocalServer() ||
            mc.getConnection() == null
        ) {
            return false;
        }
        if (ConfigManager.getConfig().enabled == false) return false;

        return true;
    }
}
