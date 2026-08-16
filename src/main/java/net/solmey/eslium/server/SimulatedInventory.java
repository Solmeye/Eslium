package net.solmey.eslium.server;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SimulatedInventory {

    public static List<ItemStack> serverInventory = new ArrayList<>();
    private static List<ItemStack> realInventory = new ArrayList<>();


    public static void startServerTick(Player player) {
        saveRealInventory(player);
        restoreServerInventory(player);
    }

    public static void endServerTick(Player player) {
        restoreRealInventory(player);

        // tick ?
    }

    public static void saveServerInventory(Player player) {
        serverInventory.clear();
        for (ItemStack itemStack : player.getInventory().items) {
            serverInventory.add(itemStack.copy());
        }
    }

    private static void restoreServerInventory(Player player) {
        for (int i = 0; i < serverInventory.size(); i++) {
            player.getInventory().items.set(i, serverInventory.get(i));
        }
    }

    private static void saveRealInventory(Player player) {
        realInventory.clear();
        for (ItemStack itemStack : player.getInventory().items) {
            realInventory.add(itemStack);
        }
    }

    private static void restoreRealInventory(Player player) {
        for (int i = 0; i < realInventory.size(); i++) {
            player.getInventory().items.set(i, realInventory.get(i));
        }
    }
}
