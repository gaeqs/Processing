package com.degoos.processing.game.network.packet.out;

import com.degoos.processing.game.network.packet.Packet;
import com.degoos.processing.game.util.StreamUtils;
import com.flowpowered.math.vector.Vector2d;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketOutOwnClientData extends Packet {

	private int entityId;
	private Vector2d position;

	public PacketOutOwnClientData(int entityId, Vector2d position) {
		this.entityId = entityId;
		this.position = position;
	}

	public PacketOutOwnClientData(DataInputStream stream) {
		try {
			entityId = stream.readInt();
			position = new Vector2d(stream.readDouble(), stream.readDouble());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write(DataOutputStream outputStream) {
		try {
			outputStream.writeInt(entityId);
			StreamUtils.writeVector(outputStream, position);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}