package net.solmey.eslium.mixin.system;

import java.util.Iterator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.solmey.eslium.Eslium;
import net.solmey.eslium.handler.PacketHandler;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/DebugScreenOverlay;showNetworkCharts()Z"
        )
    ) // Tick from the debug screen overlay that pings the server to estimate the latency
    private boolean eslium$tick(DebugScreenOverlay instance) {
        return true;
    }

    @Inject(
        method = "handleBundlePacket",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/protocol/Packet;handle(Lnet/minecraft/network/PacketListener;)V"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void eslium$handleBundlePacketBEFORE(ClientboundBundlePacket packet, CallbackInfo ci, Iterator<Packet<? super ClientGamePacketListener>> iterator, Packet<? super ClientGamePacketListener> subPacket) {
        if (!Eslium.shouldWork()) return;

        PacketHandler.head(subPacket);
    }

    @Inject(
        method = "handleBundlePacket",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/protocol/Packet;handle(Lnet/minecraft/network/PacketListener;)V",
            shift = At.Shift.AFTER
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void eslium$handleBundlePacketAFTER(ClientboundBundlePacket packet, CallbackInfo ci, Iterator<Packet<? super ClientGamePacketListener>> iterator, Packet<? super ClientGamePacketListener> subPacket) {
        if (!Eslium.shouldWork()) return;

        PacketHandler.tail(subPacket);
    }
}
