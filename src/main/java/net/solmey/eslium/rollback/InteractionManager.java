package net.solmey.eslium.rollback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.solmey.eslium.data.Data;

public class InteractionManager {

    private static List<Entity> anticheatBypassEntities = new ArrayList<>();

    public static void showEntities() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        ClientLevel clientLevel = (ClientLevel) player.level();

        List<Integer> ids = new ArrayList<>();
        for (Map.Entry<Long, Packet<ClientGamePacketListener>> entry : Data.extractedToBeValidedPackets.entries()) {
            Packet<ClientGamePacketListener> packet = entry.getValue();

            if (
                packet instanceof
                    ClientboundAddEntityPacket clientboundAddEntityPacket
            ) {
                ids.add(clientboundAddEntityPacket.getId());
            }
        }

        for (Entity entity : anticheatBypassEntities) {
            if (ids.contains(entity.getId())) {
                clientLevel.addEntity(entity);
                entity.removalReason = null; // entity.unsetRemoved();
            }
        }
        anticheatBypassEntities.clear();
    }

    public static void hideEntities() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        ClientLevel clientLevel = (ClientLevel) player.level();


        for (Map.Entry<Long, Packet<ClientGamePacketListener>> entry : Data.extractedToBeValidedPackets.entries()) {
            Packet<ClientGamePacketListener> packet = entry.getValue();

            if (
                packet instanceof
                    ClientboundAddEntityPacket clientboundAddEntityPacket
            ) {
                Entity entity = clientLevel.getEntity(
                    clientboundAddEntityPacket.getId()
                );
                if (entity != null) {
                     anticheatBypassEntities.add(entity);
                     entity.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }
}
