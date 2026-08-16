package net.solmey.eslium.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.solmey.eslium.data.Data;

public class SimulatedLevel {

    private static int fakeEntityId = Integer.MAX_VALUE;

    //private static Map<BlockPos, BlockState> simulatedBlockStates = new HashMap<>();
    private static List<Entity> newEntities = new ArrayList<>();


    public static int newEntityId() {
        return fakeEntityId--;
    }

    public static void addEntity(ClientLevel clientLevel, Entity entity) {
        entity.setId(newEntityId());
        newEntities.add(entity);

        clientLevel.addEntity(entity);
    }

    public static void tick(ClientLevel clientLevel) { // Tick server level
        for (Entity entity : newEntities) {
            clientLevel.removeEntity(entity.getId(), Entity.RemovalReason.DISCARDED);

            List<Packet<? super ClientGamePacketListener>> packets = new ArrayList<>();
      		sendPairingData(entity, packets::add);
      		Data.predictedPackets.put(Util.getMillis(), new ClientboundBundlePacket(packets));
        }
        newEntities.clear();
    }

   	private static void sendPairingData(Entity entity, Consumer<Packet<ClientGamePacketListener>> broadcast) { //From ServerEntity.java
		entity.updateDataBeforeSync();

		List<SynchedEntityData.DataValue<?>> trackedDataValues = entity.getEntityData().getNonDefaultValues();

		if (entity.isRemoved()) {
			//LOGGER.warn("Fetching packet for removed entity {}", this.entity);
		}

		Packet<ClientGamePacketListener> packet = getAddEntityPacket(entity);
		broadcast.accept(packet);
		if (trackedDataValues != null) {
			broadcast.accept(new ClientboundSetEntityDataPacket(entity.getId(), trackedDataValues));
		}

		if (entity instanceof LivingEntity livingEntity) {
			Collection<AttributeInstance> attributes = livingEntity.getAttributes().getSyncableAttributes();
			if (!attributes.isEmpty()) {
				broadcast.accept(new ClientboundUpdateAttributesPacket(entity.getId(), attributes));
			}
		}

		if (entity instanceof LivingEntity livingEntity) {
			List<Pair<EquipmentSlot, ItemStack>> slots = Lists.newArrayList();

			for (EquipmentSlot slot : EquipmentSlot.VALUES) {
				ItemStack itemStack = livingEntity.getItemBySlot(slot);
				if (!itemStack.isEmpty()) {
					slots.add(Pair.of(slot, itemStack.copy()));
				}
			}

			if (!slots.isEmpty()) {
				broadcast.accept(new ClientboundSetEquipmentPacket(entity.getId(), slots));
			}
		}

		if (!entity.getPassengers().isEmpty()) {
			broadcast.accept(new ClientboundSetPassengersPacket(entity));
		}

		if (entity.isPassenger()) {
			broadcast.accept(new ClientboundSetPassengersPacket(entity.getVehicle()));
		}

		if (entity instanceof Leashable leashable && leashable.isLeashed()) {
			broadcast.accept(new ClientboundSetEntityLinkPacket(entity, leashable.getLeashHolder()));
		}
	}

	private static Packet<ClientGamePacketListener> getAddEntityPacket(Entity entity) {
        ClientboundAddEntityPacket packet = new ClientboundAddEntityPacket(
            entity.getId(),
            entity.getUUID(),
            entity.getX(),
            entity.getY(),
            entity.getZ(),
            entity.getXRot(),
            entity.getYRot(),
            entity.getType(),
            0,
            entity.getDeltaMovement(),
            entity.getYHeadRot()
        );
        return packet;
	}
}
