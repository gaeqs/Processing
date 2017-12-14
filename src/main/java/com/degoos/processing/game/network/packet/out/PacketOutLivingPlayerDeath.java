package com.degoos.processing.game.network.packet.out;

import com.degoos.processing.game.network.packet.Packet;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class PacketOutLivingPlayerDeath extends Packet {

	private int entityId, lives;
	private boolean dead;

	public PacketOutLivingPlayerDeath(int entityId, boolean dead, int lives) {
		this.entityId = entityId;
		this.dead = dead;
		this.lives = lives;
	}

	public PacketOutLivingPlayerDeath(DataInputStream stream) {
		try {
			entityId = stream.readInt();
			dead = stream.readBoolean();
			lives = stream.readInt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getEntityId() {
		return entityId;
	}

	public boolean isDead() {
		return dead;
	}

	public int getLives() {
		return lives;
	}

	@Override
	public void write(DataOutputStream outputStream) {
		try {
			outputStream.writeInt(entityId);
			outputStream.writeBoolean(dead);
			outputStream.writeInt(lives);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
