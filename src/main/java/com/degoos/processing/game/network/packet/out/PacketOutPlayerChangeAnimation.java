package com.degoos.processing.game.network.packet.out;

import com.degoos.processing.game.enums.EnumFacingDirection;
import com.degoos.processing.game.network.packet.Packet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketOutPlayerChangeAnimation extends Packet {

	private int entityId;
	private EnumFacingDirection facingDirection;
	private boolean walking;

	public PacketOutPlayerChangeAnimation(int entityId, EnumFacingDirection facingDirection, boolean walking) {
		this.entityId = entityId;
		this.facingDirection = facingDirection;
		this.walking = walking;
	}

	public PacketOutPlayerChangeAnimation(DataInputStream stream) {
		try {
			entityId = stream.readInt();
			facingDirection = EnumFacingDirection.values()[stream.readInt()];
			walking = stream.readBoolean();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getEntityId() {
		return entityId;
	}

	public EnumFacingDirection getFacingDirection() {
		return facingDirection;
	}

	public boolean isWalking() {
		return walking;
	}

	@Override
	public void write(DataOutputStream outputStream) {
		try {
			outputStream.writeInt(entityId);
			outputStream.writeInt(facingDirection.ordinal());
			outputStream.writeBoolean(walking);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
