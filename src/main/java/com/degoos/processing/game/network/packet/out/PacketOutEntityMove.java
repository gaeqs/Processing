package com.degoos.processing.game.network.packet.out;

import com.degoos.processing.game.network.packet.Packet;
import com.degoos.processing.game.util.StreamUtils;
import com.flowpowered.math.vector.Vector2d;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class PacketOutEntityMove extends Packet {

	private int entityId;
	private Vector2d position;

	public PacketOutEntityMove(int entityId, Vector2d position) {
		this.entityId = entityId;
		this.position = position;
	}

	public PacketOutEntityMove(DataInput stream) {
		try {
			entityId = stream.readInt();
			position = new Vector2d(stream.readDouble(), stream.readDouble());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getEntityId() {
		return entityId;
	}

	public Vector2d getPosition() {
		return position;
	}

	@Override
	public void write(DataOutput outputStream) {
		try {
			outputStream.writeInt(entityId);
			StreamUtils.writeVector(outputStream, position);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


}
