package net.solmey.eslium.data;

import org.jetbrains.annotations.Nullable;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;

public class PacketPrediction {

    private Packet<ClientGamePacketListener> packet;
    private long timestamp;
    private Object realState;   // Original data of the modified thing(s) of the real world, if the predictions are applied
    private Object prediction;  // Data of the prediction

    public PacketPrediction(
        Packet<ClientGamePacketListener> packet,
        long timestamp,
        @Nullable Object realState,
        @Nullable Object prediction
    ) {
        this.packet = packet;
        this.timestamp = timestamp;
        this.realState = realState;
        this.prediction = prediction;
    }

    public Packet<ClientGamePacketListener> getPacket() {
        return packet;
    }

    public void setPacket(Packet<ClientGamePacketListener> packet) {
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
