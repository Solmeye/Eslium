package net.solmey.eslium.data;

import org.jetbrains.annotations.Nullable;

import net.minecraft.network.protocol.Packet;

public class PacketPrediction {

    private Packet<?> packet;
    private long timestamp;
    private Object realState;   // Original data of the modified thing(s) of the real world, if the predictions are applied
    private Object prediction;  // Data of state before applying the prediction

    public PacketPrediction(
        Packet<?> packet,
        long timestamp,
        @Nullable Object realState,
        @Nullable Object prediction
    ) {
        this.packet = packet;
        this.timestamp = timestamp;
        this.realState = realState;
        this.prediction = prediction;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Object getRealState() {
        return realState;
    }

    public void setRealState(Object data) {
        this.realState = data;
    }

    public Object getPrediction() {
        return prediction;
    }

    public void setPrediction(Object data) {
        this.prediction = data;
    }
}
