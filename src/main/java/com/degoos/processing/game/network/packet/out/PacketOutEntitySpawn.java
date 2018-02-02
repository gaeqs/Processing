package com.degoos.processing.game.network.packet.out;

import com.degoos.processing.game.entity.Entity;
import com.degoos.processing.game.network.packet.Packet;
import com.degoos.processing.game.util.EntitiesUtils;
import java.io.DataInput;
import java.io.DataOutput;

public class PacketOutEntitySpawn extends Packet {

	private Entity entity;

	public PacketOutEntitySpawn(Entity entity) {
		this.entity = entity;
	}

	public PacketOutEntitySpawn(DataInput stream) {
		try {
			entity = EntitiesUtils.loadEntity(stream, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Entity getEntity() {
		return entity;
	}

	@Override
	public void write(DataOutput outputStream) {
		try {
			EntitiesUtils.saveEntity(outputStream, entity);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
