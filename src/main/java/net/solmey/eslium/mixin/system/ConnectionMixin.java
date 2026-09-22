package net.solmey.eslium.mixin.system;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
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
    private void eslium$onPacketReceive(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;

        PacketPrediction packetPrediction = Data.predictions
            .stream()
            .filter(p -> p.getPacket().equals(packet))
            .findFirst()
            .orElse(null);

        if (packetPrediction == null) { // if the packet is real
            InteractionManager.removeAllPredictions();
        }
        else {  // If the packet is simulated
            InteractionManager.addAllPredictions();
        }
    }

    @Inject(
        method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V",
        at = @At("TAIL")
    )
    private void eslium$onPacketReceiveTAIL(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;

        InteractionManager.removeAllPredictions();
    }
}
