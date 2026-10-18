package net.solmey.eslium.mixin.system;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.client.multiplayer.ClientPacketListener;
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
        at = @At("HEAD")
    )
    private void eslium$handleBundlePacketHEAD(ClientboundBundlePacket packet, CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;

        PacketHandler.head(packet);
    }

    @Inject(
        method = "handleBundlePacket",
        at = @At("TAIL")
    )
    private void eslium$handleBundlePacketTAIL(ClientboundBundlePacket packet, CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;

        PacketHandler.tail(packet);
    }
}
