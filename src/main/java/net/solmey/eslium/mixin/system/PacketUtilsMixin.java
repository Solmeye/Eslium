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
        method = "ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/thread/BlockableEventLoop;executeIfPossible(Ljava/lang/Runnable;)V"
        )
    )
    private static <T extends PacketListener> void eslium$ensureRunningOnSameThreadBEFORE(Packet<?> packet, PacketListener packetListener, BlockableEventLoop<?> blockableEventLoop, CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;

        PacketHandler.head(packet);
    }

    @Inject(
        method = "ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/thread/BlockableEventLoop;executeIfPossible(Ljava/lang/Runnable;)V",
            shift = At.Shift.AFTER
        )
    )
    private static <T extends PacketListener> void eslium$ensureRunningOnSameThreadAFTER(Packet<?> packet, PacketListener packetListener, BlockableEventLoop<?> blockableEventLoop, CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;

        PacketHandler.tail(packet);
    }
}
