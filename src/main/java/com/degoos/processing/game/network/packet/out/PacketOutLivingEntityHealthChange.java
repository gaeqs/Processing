package com.degoos.processing.game.network.packet.out;

import com.degoos.processing.game.network.packet.Packet;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class PacketOutLivingEntityHealthChange extends Packet {

	private int entityId;
	private double oldHealth, newHealth;

	public PacketOutLivingEntityHealthChange(int entityId, double oldHealth, double newHealth) {
		this.entityId = entityId;
		this.oldHealth = oldHealth;
		this.newHealth = newHealth;
	}

	public PacketOutLivingEntityHealthChange(DataInputStream stream) {
		try {
			entityId = stream.readInt();
			oldHealth = stream.readDouble();
			newHealth = stream.readDouble();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getEntityId() {
		return entityId;
	}

	public double getOldHealth() {
		return oldHealth;
	}

	public double getNewHealth() {
		return newHealth;
	}

	@Override
	public void write(DataOutputStream outputStream) {
		try {
			outputStream.writeInt(entityId);
			outputStream.writeDouble(oldHealth);
			outputStream.writeDouble(newHealth);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
