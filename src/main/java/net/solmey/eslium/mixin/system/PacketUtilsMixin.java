package net.solmey.eslium.mixin.system;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.thread.BlockableEventLoop;

import net.minecraft.network.PacketListener;
import net.solmey.eslium.Eslium;
import net.solmey.eslium.handler.PacketHandler;

@Mixin(PacketUtils.class)
public class PacketUtilsMixin {

    @Inject(
        method = "ensureRunningOnSameThread",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/protocol/Packet;handle(Lnet/minecraft/network/PacketListener;)V"
        )
    )
    private void eslium$handleBEFORE(Packet<?> packet, PacketListener packetListener, BlockableEventLoop<?> blockableEventLoop, CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;

        PacketHandler.head(packet);
    }

    @Inject(
        method = "ensureRunningOnSameThread",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/protocol/Packet;handle(Lnet/minecraft/network/PacketListener;)V",
            shift = At.Shift.AFTER
        )
    )
    private void eslium$handleAFTER(Packet<?> packet, PacketListener packetListener, BlockableEventLoop<?> blockableEventLoop, CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;

        PacketHandler.tail(packet);
    }
}
