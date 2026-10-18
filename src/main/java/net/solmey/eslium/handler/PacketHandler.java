package net.solmey.eslium.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.solmey.eslium.data.Data;
import net.solmey.eslium.data.PacketPrediction;
import net.solmey.eslium.interactions.InteractionManager;

public class PacketHandler {

    public static void head(Packet<?> packet) {
        if (!Minecraft.getInstance().isSameThread())
            return;

        InteractionManager.blockthread();

        PacketPrediction packetPrediction = Data.predictions.stream()
            .filter(prediction -> prediction.getPacket().equals(packet) && !prediction.isHandled())
            .findFirst()
            .orElse(null);

        if(packetPrediction != null) {  // If the packet is simulated

            if(packet instanceof ClientboundAddEntityPacket rPacket) {
                Minecraft mc = Minecraft.getInstance();
                ClientLevel clientLevel = mc.level;
                Entity entity = clientLevel.getEntity(rPacket.getId());
                packetPrediction.setRealState(entity);  // Assign the real state before it gets potentially overwritten by the packet

            }
            else if (packet instanceof ClientboundSetEntityDataPacket rPacket) {

            }

            InteractionManager.addAllPredictions();
        }
        else { // if the packet is real
            // InteractionManager.removeAllPredictions();
        }
    }

    public static void tail(Packet<?> packet) {
        if (!Minecraft.getInstance().isSameThread())
            return;

        PacketPrediction packetPrediction = Data.predictions.stream()
            .filter(prediction -> prediction.getPacket().equals(packet) && !prediction.isHandled())
            .findFirst()
            .orElse(null);

        if(packetPrediction != null) { // If the packet is simulated

            if(packet instanceof ClientboundAddEntityPacket rPacket) {
                Minecraft mc = Minecraft.getInstance();
                ClientLevel clientLevel = mc.level;
                Entity entity = clientLevel.getEntity(rPacket.getId());
                packetPrediction.setPrediction(entity);  // Assign the predicted packet behavior to the PacketPrediction object
            }
            else if (packet instanceof ClientboundSetEntityDataPacket rPacket) {

            }

            packetPrediction.setHandled(true);
        }
        else {

            PacketPrediction matchingPrediction = null;

            // Try to find a matching prediction
            if(packet instanceof ClientboundAddEntityPacket rPacket) {

                for (PacketPrediction prediction : Data.predictions) {
                    if (
                        prediction.getPacket() instanceof ClientboundAddEntityPacket cPacket &&

                        rPacket.getType().equals(cPacket.getType()) &&
                        rPacket.getX() == cPacket.getX() &&
                        rPacket.getY() == cPacket.getY() &&
                        rPacket.getZ() == cPacket.getZ() &&
                        rPacket.getXRot() == cPacket.getXRot() &&
                        rPacket.getYRot() == cPacket.getYRot() &&
                        rPacket.getYHeadRot() == cPacket.getYHeadRot()
                    ) {
                        // Keep some predicted state / data to avoid any visual issue
                        Minecraft mc = Minecraft.getInstance();
                        ClientLevel clientLevel = mc.level;
                        Entity entity = clientLevel.getEntity(rPacket.getId());
                        if(
                            prediction.getPrediction()  instanceof EndCrystal realEntity &&
                            entity                      instanceof EndCrystal endCrystal
                        ) {
                            endCrystal.time = realEntity.time;
                        }


                        matchingPrediction = prediction;
                        break;
                    }
                }
            }
            else if (packet instanceof ClientboundSetEntityDataPacket rPacket) {

            }
            else {

            }

            if(matchingPrediction != null) {
                System.out.println("rollback" + packet);
                InteractionManager.rollback(matchingPrediction);
            }
        }
        InteractionManager.unblockthread();
    }
}