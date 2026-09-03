package net.solmey.eslium.mixin;

import java.util.Iterator;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.solmey.eslium.Eslium;
import net.solmey.eslium.config.ConfigManager;
import net.solmey.eslium.data.Data;
import net.solmey.eslium.predictions.UseItemOnPacket;
import net.solmey.eslium.rollback.InteractionManager;
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

        // Calculate the timestamp of the next server tick

        long MSPTnano = Minecraft.getInstance()
            .level.tickRateManager()
            .nanosecondsPerTick();

        int desync = Math.clamp(ConfigManager.getConfig().simulatedDesync, 0, 100);
        MSPTnano = MSPTnano * desync / 100;

        Data.timestampNanoNextServerTick = System.nanoTime() + MSPTnano;
    }

    @Inject(method = "tick", at = @At("TAIL")) // End of the client tick, start of the server tick
    private void eslium$tickTAIL(CallbackInfo callback) {
        if (!Eslium.shouldWork()) return;
        LocalPlayer player = Minecraft.getInstance().player;
        ClientLevel clientLevel = (ClientLevel) player.level();

        MixinMode.mixinMode = true;
        InteractionManager.showEntities();



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



        InteractionManager.hideEntities();
        MixinMode.mixinMode = false;



        // If the real packet from the server is received before handling it, we need to rollback anyways
        Data.extractPackets(Data.predictedPackets);
        Data.extractedToBeValidedPackets.putAll(Data.predictedPackets);
    }

    @Inject(method = "runTick", at = @At("HEAD")) // Each frame
    private void eslium$runTick(boolean advanceGameTime, CallbackInfo ci) {
        if (!Eslium.shouldWork()) return;

        if (
            Data.timestampNanoNextServerTick != -1 &&
            System.nanoTime() >= Data.timestampNanoNextServerTick
        ) {
            Data.timestampNanoNextServerTick = -1;

            ClientPacketListener connection = Minecraft.getInstance().getConnection();

            for (var entry : Data.predictedPackets.entries()) {
                Packet<ClientGamePacketListener> packet = entry.getValue();

                if(Data.extractedToBeValidedPackets.containsValue(packet)) {
                    packet.handle(connection);
                }
            }
            Data.predictedPackets.clear();

            InteractionManager.hideEntities();
        }


        //InteractionManager.showEntities();
        Iterator<Map.Entry<Long, Packet<ClientGamePacketListener>>> iterator =
                Data.extractedToBeValidedPackets.entries().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Packet<ClientGamePacketListener>> entry = iterator.next();

            long timestamp = entry.getKey();
            Packet<ClientGamePacketListener> packet = entry.getValue();

            float MSPT = Minecraft.getInstance()
                .level.tickRateManager()
                .millisecondsPerTick();

            if (MixinMode.lastTimestamp > timestamp + MSPT * 2) {
                //System.out.println("TIMEOUT\n");
                //Rollback.rollback(packet);
                iterator.remove();
            }
        }
        //InteractionManager.hideEntities();
    }

    /*@Inject(
            method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;ZZ)V",
            at = @At("HEAD")
    ) // When the client is disconnected from the server
    private void eslium$disconnect(Screen screen, boolean keepResourcePacks, boolean stopSound, CallbackInfo ci) {

    }*/
}
