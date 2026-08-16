package net.solmey.eslium.rollback;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;

public class Rollback {

    public static void rollback(Packet<ClientGamePacketListener> packet, @Nullable Packet<ClientGamePacketListener> realPacket) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ClientLevel clientLevel = (ClientLevel) player.level();


        if(packet instanceof ClientboundAddEntityPacket clientboundAddEntityPacket) {
            Entity entity = null;
            Entity realEntity = null;

            //ClientboundAddEntityPacket clientboundAddEntityPacket = null ;
            ClientboundAddEntityPacket realClientboundAddEntityPacket = null;
            if(realPacket instanceof ClientboundAddEntityPacket tempPacket) {
                realClientboundAddEntityPacket = tempPacket;
            }

            entity = clientLevel.getEntity(clientboundAddEntityPacket.getId());
            if(realClientboundAddEntityPacket != null) {
                realEntity = clientLevel.getEntity(realClientboundAddEntityPacket.getId());
            }




            if(entity instanceof EndCrystal endCrystal && realEntity instanceof EndCrystal realEndCrystal) {
                realEndCrystal.time = endCrystal.time;
            }
            clientLevel.removeEntity(clientboundAddEntityPacket.getId(), Entity.RemovalReason.DISCARDED);
        }
    }

    public static void rollback(Packet<ClientGamePacketListener> packet) {
        rollback(packet, null);
    }
}
