package net.solmey.eslium.interactions;

import java.util.concurrent.Semaphore;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.solmey.eslium.data.Data;
import net.solmey.eslium.data.PacketPrediction;

public class InteractionManager {

    private static final Semaphore semaphore = new Semaphore(1);
    // When the game interact with the world you may need to add or remove every predictions, then allow it to do whatever he wants
    // But between the time it adds or removes predictions another thread may modify the world
    // That's why semaphore is here : to block the a thread until it gets the authorization to run its code

    private static boolean predictionApplied;

    public static void blockthread() {
        semaphore.acquireUninterruptibly();
    }

    public static void unblockthread() {
        semaphore.release();
    }

	// Remove the prediction linked to the packet and the packet itself
    public static void rollback(PacketPrediction packetPrediction) {
        boolean temp = predictionApplied;

        removeAllPredictions();
        Data.predictions.remove(packetPrediction); // If we rollback only 1 prediction, it may overwrite another prediction, so here is the fix (up and down):
        if(temp) {
            addAllPredictions();
        }
    }

    public static void addAllPredictions() {

        if(predictionApplied)
            return;

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

        predictionApplied = true;
    }

    // Remove all predictions from the client game
    public static void removeAllPredictions() {

        if(!predictionApplied)
            return;

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

        predictionApplied = false;
    }
}
