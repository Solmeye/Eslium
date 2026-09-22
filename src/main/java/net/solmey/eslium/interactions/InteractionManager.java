package net.solmey.eslium.interactions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.solmey.eslium.data.Data;
import net.solmey.eslium.data.PacketPrediction;

public class InteractionManager {

    private static boolean predictionApplied;

    public static boolean isPredictionApplied() {
		return predictionApplied;
	}

	public static void setPredictionApplied(boolean state) {
		predictionApplied = state;
	}


	// Remove the prediction linked to the packet and the packet itself
    public static void rollback(Packet<?> packet) {

        boolean temp = predictionApplied;

        // If we rollback only 1 prediction, it may overwrite another prediction, so here is the fix :
        removeAllPredictions();
        Data.predictions.removeIf(p -> p.getPacket().equals(packet));
        if(temp)
            addAllPredictions();
    }

    public static void addAllPredictions() {
        if(predictionApplied)
            return;

        predictionApplied = true;

        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel clientLevel = minecraft.level;

        for (PacketPrediction packetPrediction : Data.predictions) {
            Object prediction = packetPrediction.getPrediction();

            // Apply the prediction
            if(prediction instanceof Entity entity) {
                clientLevel.addEntity(entity);
                entity.removalReason = null; // entity.unsetRemoved();
            }
            else {

            }

            // Save real state
            if(prediction instanceof Entity entity) {
                Entity realEntity = clientLevel.getEntity(entity.getId());
                packetPrediction.setRealState(realEntity);
            }
            else {

            }
        }
    }

    // Remove all predictions from the client game
    public static void removeAllPredictions() {
        predictionApplied = false;

        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel clientLevel = minecraft.level;

        for (int i = Data.predictions.size() - 1; i >= 0; i--) {
            PacketPrediction packetPrediction = Data.predictions.get(i);
            // Remove the prediction
            Object prediction = packetPrediction.getPrediction();

            // Sometimes the realstate is null - removing the prediction first is needed
            if(prediction instanceof Entity entity) {
                entity.remove(Entity.RemovalReason.DISCARDED);
            }
            else {

            }

            // Apply and clear the realState
            Object realState = packetPrediction.getRealState();

            if(realState instanceof Entity realEntity) {
                clientLevel.addEntity(realEntity);
                realEntity.removalReason = null;
            }
            else {

            }
            packetPrediction.setRealState(null);
        }
    }
}
