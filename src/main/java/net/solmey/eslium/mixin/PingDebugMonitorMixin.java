package net.solmey.eslium.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.multiplayer.PingDebugMonitor;
import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
import net.solmey.eslium.Eslium;
import net.solmey.eslium.server.MixinMode;

@Mixin(PingDebugMonitor.class)
public class PingDebugMonitorMixin {

    @Inject(method = "onPongReceived", at = @At("HEAD"))
    private void eslium$onPongReceived(ClientboundPongResponsePacket pongPacket, CallbackInfo callback) {
        if (!Eslium.shouldWork())
            return;

        MixinMode.lastTimestamp = pongPacket.time();
    }
}
