package net.solmey.eslium.mixin.system;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.solmey.eslium.Eslium;
import net.solmey.eslium.config.ConfigManager;
import net.solmey.eslium.data.Data;
import net.solmey.eslium.data.PacketPrediction;
import net.solmey.eslium.interactions.InteractionManager;
import net.solmey.eslium.predictions.UseItemOnPacket;
import net.solmey.eslium.server.MixinMode;
import net.solmey.eslium.server.SimulatedInventory;
import net.solmey.eslium.server.SimulatedLevel;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "tick", at = @At("HEAD")) // Start of the client tick
    private void eslium$tickHEAD(CallbackInfo callback) {
        if (!Eslium.shouldWork()) return;

        LocalPlayer player = Minecraft.getInstance().player;
        SimulatedInventory.saveServerInventory(player);
    }

    @Inject(method = "tick", at = @At("TAIL")) // End of the client tick, start of the server tick
    private void eslium$tickTAIL(CallbackInfo callback) {
        if (!Eslium.shouldWork()) return;
        LocalPlayer player = Minecraft.getInstance().player;
        ClientLevel clientLevel = (ClientLevel) player.level();

        MixinMode.mixinMode = true;
        InteractionManager.blockthread();
        InteractionManager.addAllPredictions();



        SimulatedInventory.startServerTick(player);



        // Server handling packet tick or whatever I should call that
        synchronized (Data.sentPackets) {
            for (Packet<?> packet : Data.sentPackets) {
                UseItemOnPacket.onSentPacket(packet);
            }
            Data.sentPackets.clear();
        }


        // Server tick
        SimulatedInventory.endServerTick(player);
        SimulatedLevel.tick(clientLevel);



        InteractionManager.unblockthread();
        MixinMode.mixinMode = false;



        // We need to keep track of the simulated packets to link them to the predictions (to detect rollbacks with packets)
        Data.extractPackets(Data.predictedPackets); // Extract all packets from predictedPackets
        InteractionManager.blockthread();
        for (var packet : Data.predictedPackets) {
            Data.predictions.add(new PacketPrediction(packet, System.nanoTime(), null, null, false));
        }
        InteractionManager.unblockthread();
        Data.predictedPackets.clear();
    }

    @Inject(method = "runTick", at = @At("HEAD")) // Each frame
    private void eslium$runTick(boolean advanceGameTime, CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;

        long MSPT = Minecraft.getInstance()
            .level.tickRateManager()
            .nanosecondsPerTick();

        // Handle packets
        Connection connection = Minecraft.getInstance().pendingConnection;
        if(connection != null) {
            PacketListener packetListener = connection.getPacketListener();

            // Calculate the desync / delay before applying a prediction
            int desync = ConfigManager.getConfig().simulatedDesync;
            long delay = MSPT * desync / 100;

            InteractionManager.blockthread();
            for (PacketPrediction packetPrediction : Data.predictions) {
                if(!packetPrediction.isHandled() &&
                    System.nanoTime() >= (packetPrediction.getTimestamp() + delay)
                ) {
                    Connection.genericsFtw(packetPrediction.getPacket(), packetListener); // Handle the packet
                    packetPrediction.setHandled(true);
                }
            }
            InteractionManager.unblockthread();
        }

        // Timeout check
        float margin = MSPT * 2;

        InteractionManager.blockthread();
        for (PacketPrediction packetPrediction : Data.predictions) {

            if(MixinMode.lastTimestampNano > packetPrediction.getTimestamp() + margin) {
                InteractionManager.rollback(packetPrediction);
            }
        }
        InteractionManager.unblockthread();
    }

    @Inject(
            method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;ZZ)V",
            at = @At("HEAD")
    ) // When the client is disconnected from the server
    private void eslium$disconnect(Screen screen, boolean keepResourcePacks, boolean stopSound, CallbackInfo ci) {
        Data.sentPackets.clear();
        Data.predictedPackets.clear();
        Data.predictions.clear();
    }
}
