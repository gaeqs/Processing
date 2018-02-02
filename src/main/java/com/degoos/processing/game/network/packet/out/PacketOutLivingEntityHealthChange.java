package com.degoos.processing.game.network.packet.out;

import com.degoos.processing.game.network.packet.Packet;
import java.io.DataInput;
import java.io.DataOutput;

public class PacketOutLivingEntityHealthChange extends Packet {

	private int entityId;
	private double oldHealth, newHealth;

	public PacketOutLivingEntityHealthChange(int entityId, double oldHealth, double newHealth) {
		this.entityId = entityId;
		this.oldHealth = oldHealth;
		this.newHealth = newHealth;
	}

	public PacketOutLivingEntityHealthChange(DataInput stream) {
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
	public void write(DataOutput outputStream) {
		try {
			outputStream.writeInt(entityId);
			outputStream.writeDouble(oldHealth);
			outputStream.writeDouble(newHealth);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
