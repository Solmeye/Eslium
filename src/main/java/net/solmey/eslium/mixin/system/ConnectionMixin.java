package net.solmey.eslium.mixin.system;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.solmey.eslium.Eslium;
import net.solmey.eslium.data.Data;
import net.solmey.eslium.data.PacketPrediction;
import net.solmey.eslium.interactions.InteractionManager;

@Mixin(Connection.class)
public class ConnectionMixin {

    @Inject(
        method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;Z)V",
        at = @At("TAIL")
    ) // When a packet is sent from the client
    private void eslium$send(
        Packet<?> packet,
        @Nullable ChannelFutureListener listener,
        boolean flush,
        CallbackInfo callback
    ) {
        if (!Eslium.shouldWork()) return;

        synchronized (Data.sentPackets) {
            Data.sentPackets.add(packet);
        }
    }

    @Inject(
        method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V",
        at = @At("HEAD")
    )
    private void eslium$channelRead0HEAD(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;

        InteractionManager.blockthread();

        PacketPrediction packetPrediction = Data.predictions.stream()
            .filter(prediction -> prediction.getPacket().equals(packet))
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
            InteractionManager.removeAllPredictions();
        }
    }

    @Inject(
        method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V",
        at = @At("TAIL")
    )
    private void eslium$channelRead0TAIL(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;


        PacketPrediction packetPrediction = Data.predictions.stream()
            .filter(prediction -> prediction.getPacket().equals(packet))
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

            InteractionManager.removeAllPredictions();
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
                InteractionManager.rollback(matchingPrediction);
            }
        }
        InteractionManager.unblockthread();
    }
}