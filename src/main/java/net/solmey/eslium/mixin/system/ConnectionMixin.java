package net.solmey.eslium.mixin.system;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.channel.ChannelFutureListener;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.solmey.eslium.Eslium;
import net.solmey.eslium.data.Data;
import net.solmey.eslium.handler.PacketHandler;

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
        method = "genericsFtw(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;)V",
        at = @At("HEAD")
    )
    private static void eslium$genericsFtwHEAD(Packet<?> packet, PacketListener listener, CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;

        PacketHandler.head(packet);
    }

    @Inject(
        method = "genericsFtw(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;)V",
        at = @At("TAIL")
    )
    private static void eslium$genericsFtwTAIL(Packet<?> packet, PacketListener listener, CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;

        PacketHandler.tail(packet);
    }
}
