package net.solmey.eslium;


import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.solmey.eslium.config.ConfigManager;

@Mod("eslium")
public class Eslium {

   	public static final String MOD_ID = "eslium";

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
