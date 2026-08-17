package net.solmey.eslium.server;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;

public class SimulatedCooldowns {

    private static ItemCooldowns serverItemCooldowns = new ItemCooldowns();
    private static ItemCooldowns realItemCooldowns = new ItemCooldowns();


    public static void startServerTick(Player player) {
        saveRealCooldowns(player);
        restoreServerCooldowns(player);
    }

    public static void endServerTick(Player player) {
        restoreRealCooldowns(player);
    }

    public static void saveServerCooldowns(Player player) {
        serverItemCooldowns = new ItemCooldowns();
        ItemCooldowns playerCooldowns = player.getCooldowns();

        serverItemCooldowns.tickCount = playerCooldowns.tickCount;
        serverItemCooldowns.cooldowns.putAll(playerCooldowns.cooldowns);
    }

    private static void restoreServerCooldowns(Player player) {
        ItemCooldowns playerCooldowns = player.getCooldowns();

        playerCooldowns.tickCount = serverItemCooldowns.tickCount;
        playerCooldowns.cooldowns.putAll(serverItemCooldowns.cooldowns);
    }

    private static void saveRealCooldowns(Player player) {
        realItemCooldowns = new ItemCooldowns();
        ItemCooldowns playerCooldowns = player.getCooldowns();

        realItemCooldowns.tickCount = playerCooldowns.tickCount;
        realItemCooldowns.cooldowns.putAll(playerCooldowns.cooldowns);
    }

    private static void restoreRealCooldowns(Player player) {
        ItemCooldowns playerCooldowns = player.getCooldowns();

        playerCooldowns.tickCount = realItemCooldowns.tickCount;
        playerCooldowns.cooldowns.putAll(realItemCooldowns.cooldowns);
    }
}
