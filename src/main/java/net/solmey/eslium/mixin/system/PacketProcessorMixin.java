package net.solmey.eslium.mixin.system;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.PacketProcessor;
import net.minecraft.network.PacketProcessor.ListenerAndPacket;
import net.minecraft.network.protocol.Packet;
import net.solmey.eslium.Eslium;
import net.solmey.eslium.handler.PacketHandler;

@Mixin(PacketProcessor.ListenerAndPacket.class)
public class PacketProcessorMixin {

    @Inject(
        method = "handle",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/protocol/Packet;handle(Lnet/minecraft/network/PacketListener;)V"
        )
    )
    private void eslium$handleBEFORE(CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;

        ListenerAndPacket listenerAndPacketAccessor = (ListenerAndPacket) (Object) this;
        Packet<?> packet = listenerAndPacketAccessor.packet;

        PacketHandler.head(packet);
    }

    @Inject(
        method = "handle",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/protocol/Packet;handle(Lnet/minecraft/network/PacketListener;)V",
            shift = At.Shift.AFTER
        )
    )
    private void eslium$handleAFTER(CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;

        ListenerAndPacket listenerAndPacketAccessor = (ListenerAndPacket) (Object) this;
        Packet<?> packet = listenerAndPacketAccessor.packet;

        PacketHandler.tail(packet);
    }
}
