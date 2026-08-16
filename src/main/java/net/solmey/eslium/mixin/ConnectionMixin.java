package net.solmey.eslium.mixin;

//import javax.annotation.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.channel.ChannelFutureListener;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.solmey.eslium.Eslium;
import net.solmey.eslium.data.Data;

@Mixin(Connection.class)
public class ConnectionMixin {

    @Inject(
        method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;Z)V",
        at = @At("TAIL")
    ) // When a packet is sent from the client
    private void eslium$send(
        Packet<?> packet,
        CallbackInfo callback
    ) {
        if (!Eslium.shouldWork()) return;

        synchronized (Data.sentPackets) {
            Data.sentPackets.add(packet);
        }
    }
}
