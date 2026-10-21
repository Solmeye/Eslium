package net.solmey.eslium;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.solmey.eslium.config.ConfigManager;

public class Eslium {

    public static String id = "eslium";

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
