package net.solmey.eslium.mixin;

import java.util.Iterator;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.solmey.eslium.Eslium;
import net.solmey.eslium.data.Data;
import net.solmey.eslium.rollback.InteractionManager;
import net.solmey.eslium.rollback.Rollback;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/DebugScreenOverlay;showNetworkCharts()Z"
        )
    ) // Tick from the debug screen overlay that pings the server to estimate the latency
    private boolean eslium$showNetworkCharts(DebugScreenOverlay instance) {
        return true;
    }

    /*@Inject(
        method = "handleAddEntity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/network/PacketProcessor;)V",
            shift = At.Shift.AFTER
        )
    )*/
    @Inject(method = "handleAddEntity", at = @At("TAIL"))
    private void eslium$handleAddEntity(
        ClientboundAddEntityPacket packet,
        CallbackInfo ci
    ) {
        if (!Eslium.shouldWork()) return;

        if(Data.extractedToBeValidedPackets.containsValue(packet))
            return;

        Iterator<Map.Entry<Long, Packet<ClientGamePacketListener>>> iterator =
                Data.extractedToBeValidedPackets.entries().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Packet<ClientGamePacketListener>> entry = iterator.next();
            Packet<ClientGamePacketListener> packetToCheck = entry.getValue();


            if(packetToCheck instanceof ClientboundAddEntityPacket cPacket) {
                if(
                    packet.getType().equals( cPacket.getType() )  &&
                    packet.getX() == cPacket.getX()               &&
                    packet.getY() == cPacket.getY()               &&
                    packet.getZ() == cPacket.getZ()               &&
                    packet.getXRot() == cPacket.getXRot()         &&
                    packet.getYRot() == cPacket.getYRot()         &&
                    packet.getYHeadRot() == cPacket.getYHeadRot()
                ) {
                    //System.out.println("ROLLBACK\n");
                    InteractionManager.showEntities();
                    Rollback.rollback(cPacket, packet);
                    InteractionManager.hideEntities();


                    iterator.remove();
                    return;
                }
            }
        }
    }
}
