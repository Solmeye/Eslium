package net.solmey.eslium.data;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;

public class Data {

    public static long lastTimestampNano;

    public static List<Packet<?>>                           sentPackets         = new ArrayList<>();    // Packets sent to the server
    public static List<Packet<?>>                           predictedPackets    = new ArrayList<>();    // Simulated packets to extract and package in predictions
    public static List<PacketPrediction>                    predictions         = new ArrayList<>();    // Predictions

    public static void extractPackets(List<Packet<?>> list) {
        List<Packet<?>> tempList = new ArrayList<>();
        tempList.addAll(list);
        list.clear();

        for(var packet : tempList){
            extractPacket(list, packet);
        }
    }

    private static void extractPacket(List<Packet<?>> list, Packet<?> packet) {
        if (packet instanceof ClientboundBundlePacket bundlePacket) {
            for (Packet<?> subPacket : bundlePacket.subPackets()) {
                extractPacket(list, subPacket);
            }
        } else {
            list.add(packet);
        }
    }
}
