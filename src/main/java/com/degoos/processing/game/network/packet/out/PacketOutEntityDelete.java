package com.degoos.processing.game.network.packet.out;

import com.degoos.processing.game.network.packet.Packet;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class PacketOutEntityDelete extends Packet {

	private int entityId;

	public PacketOutEntityDelete(int entityId) {
		this.entityId = entityId;
	}

	public PacketOutEntityDelete(DataInput stream) {
		try {
			entityId = stream.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getEntityId() {
		return entityId;
	}

	@Override
	public void write(DataOutput outputStream) {
		try {
			outputStream.writeInt(entityId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
