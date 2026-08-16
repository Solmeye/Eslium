package net.solmey.eslium.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;

public class Data {

    public static long timestampNanoNextServerTick = -1;

    public static List<Packet<?>> sentPackets = new ArrayList<>(); // Packets that the client sent to the server
    public static Multimap<Long, Packet<ClientGamePacketListener>> predictedPackets = ArrayListMultimap.create(); // Packet predicted by the client, but not used yet
    public static Multimap<Long, Packet<ClientGamePacketListener>> extractedToBeValidedPackets = ArrayListMultimap.create();




    public static void extractPackets(Multimap<Long, Packet<ClientGamePacketListener>> multimap) {
        Multimap<Long, Packet<ClientGamePacketListener>> tempMultimap = ArrayListMultimap.create();
        tempMultimap.putAll(multimap);

        multimap.clear();

        Iterator<Map.Entry<Long, Packet<ClientGamePacketListener>>> iterator = tempMultimap.entries().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Long, Packet<ClientGamePacketListener>> entry = iterator.next();

            extractPacket(multimap, entry.getKey(), entry.getValue());
        }
    }


    private static void extractPacket(Multimap<Long, Packet<ClientGamePacketListener>> multimap, long timestamp, Packet<ClientGamePacketListener> packet) {
        if (packet instanceof ClientboundBundlePacket bundlePacket) {
            for (Packet<?> subPacket : bundlePacket.subPackets()) {
                extractPacket(multimap, timestamp, (Packet<ClientGamePacketListener>) subPacket);
            }
        } else {
            multimap.put(timestamp, packet);
        }
    }
}
