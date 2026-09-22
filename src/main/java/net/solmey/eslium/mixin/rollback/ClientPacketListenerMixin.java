package net.solmey.eslium.mixin.rollback;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.solmey.eslium.Eslium;
import net.solmey.eslium.data.Data;
import net.solmey.eslium.data.PacketPrediction;
import net.solmey.eslium.interactions.InteractionManager;

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
    )
    private void eslium$handleAddEntity(
        ClientboundAddEntityPacket packet,
        CallbackInfo ci
    ) {
        if (!Eslium.shouldWork()) return;


        PacketPrediction packetPrediction = Data.predictions
            .stream()
            .filter(p -> p.getPacket().equals(packet))
            .findFirst()
            .orElse(null);

        if (packetPrediction == null) { // if the packet is real
            InteractionManager.removeAllPredictions(); // Or the real behavior might be overwritten if it is called later in the code
        }
        else {

            //InteractionManager.removeAllPredictions(); // Not doing it will result in storing a prediction as a real state

            // Save the real state before the prediction is applied
            //packetPrediction.setRealState(null); // There shouldn't have any entity in the real world, so we simplify this part with null



            InteractionManager.addAllPredictions(); // Predicted actions interact with other predictions, shouldn't with the real world
        }
    }*/

    /*@Inject(
        method = "handleAddEntity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/network/PacketProcessor;)V",
            shift = At.Shift.AFTER
        )
    )*/
    @Inject(method = "handleAddEntity", at = @At("TAIL"))
    private void eslium$handleAddEntityTAIL(
        ClientboundAddEntityPacket packet,
        CallbackInfo ci
    ) {
        if (!Eslium.shouldWork()) return;


        ClientPacketListener clientPacketListener = (ClientPacketListener) (Object) this;
        ClientLevel clientLevel = clientPacketListener.getLevel();
        Entity entity = clientLevel.getEntity(packet.getId());

        PacketPrediction packetPrediction = Data.predictions
            .stream()
            .filter(p -> p.getPacket().equals(packet))
            .findFirst()
            .orElse(null);

        if (packetPrediction == null) {

            // Find the correlated prediction

            for (PacketPrediction prediction : Data.predictions) {
                if (
                    prediction.getPacket() instanceof ClientboundAddEntityPacket cPacket &&

                    packet.getType().equals(cPacket.getType()) &&
                    packet.getX() == cPacket.getX() &&
                    packet.getY() == cPacket.getY() &&
                    packet.getZ() == cPacket.getZ() &&
                    packet.getXRot() == cPacket.getXRot() &&
                    packet.getYRot() == cPacket.getYRot() &&
                    packet.getYHeadRot() == cPacket.getYHeadRot()
                ) {
                    if(prediction.getPrediction() instanceof EndCrystal realEntity &&
                        entity instanceof EndCrystal endCrystal) {
                        endCrystal.time = realEntity.time;
                    }

                    InteractionManager.rollback(prediction.getPacket());

                    break;
                }
            }
        }
        else {
            packetPrediction.setPrediction(entity); // Assign the predicted packet behavior to the PacketPrediction object
        }
    }
}
