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
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
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

    private final ThreadLocal<PacketPrediction> matchingPrediction = new ThreadLocal<>();

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
    private void eslium$onPacketReceive(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;

        matchingPrediction.set(null);

        if(Data.predictions
            .stream()
            .anyMatch(packetPrediction -> packetPrediction.getPacket().equals(packet))
        ) {  // If the packet is simulated
            InteractionManager.addAllPredictions();
        }
        else { // if the packet is real

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
                        matchingPrediction.set(prediction);

                        break;
                    }
                }
            }
            else if (packet instanceof ClientboundSetEntityDataPacket rPacket) {

            }

            if(matchingPrediction.get() != null) {
                InteractionManager.removeAllPredictions();
            }
        }
    }

    @Inject(
        method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V",
        at = @At("TAIL")
    )
    private void eslium$onPacketReceiveTAIL(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;


        PacketPrediction packetPrediction = Data.predictions.stream()
            .filter(prediction -> prediction.getPacket().equals(packet))
            .findFirst()
            .orElse(null);

        if(packetPrediction != null) { // If the packet is simulated

            Minecraft mc = Minecraft.getInstance();
            ClientLevel clientLevel = mc.level;

            if(packet instanceof ClientboundAddEntityPacket rPacket) {
                Entity entity = clientLevel.getEntity(rPacket.getId());
                packetPrediction.setPrediction(entity);  // Assign the predicted packet behavior to the PacketPrediction object
            }
            else if (packet instanceof ClientboundSetEntityDataPacket rPacket) {

            }

            InteractionManager.removeAllPredictions();
        }
        else {
            InteractionManager.rollback(matchingPrediction.get());
            // Rollback da prediction
            //
            // Keep some predicted state / data to avoid any visual issue
            /*if(prediction.getPrediction() instanceof EndCrystal realEntity &&
                entity instanceof EndCrystal endCrystal) {
                endCrystal.time = realEntity.time;
            }*/
        }
    }
}

// packetPrediction.setPrediction(entity); // Assign the predicted packet behavior to the PacketPrediction object

// ClientPacketListener clientPacketListener = (ClientPacketListener) (Object) this;
// ClientLevel clientLevel = clientPacketListener.getLevel();
// Entity entity = clientLevel.getEntity(rPacket.getId());
