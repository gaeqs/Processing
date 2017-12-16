package com.degoos.processing.game.network.packet;

import com.degoos.processing.game.network.packet.in.PacketInPressKey;
import com.degoos.processing.game.network.packet.in.PacketInReleaseKey;
import com.degoos.processing.game.network.packet.out.PacketOutEntityDelete;
import com.degoos.processing.game.network.packet.out.PacketOutEntityMove;
import com.degoos.processing.game.network.packet.out.PacketOutEntitySpawn;
import com.degoos.processing.game.network.packet.out.PacketOutLivingEntityHealthChange;
import com.degoos.processing.game.network.packet.out.PacketOutLivingPlayerDeath;
import com.degoos.processing.game.network.packet.out.PacketOutOwnClientData;
import com.degoos.processing.game.network.packet.out.PacketOutPlayerChangeAnimation;
import java.util.HashMap;

public class PacketMap extends HashMap<Short, Class<? extends Packet>> {

	public PacketMap() {
		super();
		putPacket(0, PacketInPressKey.class);
		putPacket(1, PacketInReleaseKey.class);
		putPacket(2, PacketOutEntityDelete.class);
		putPacket(3, PacketOutEntityMove.class);
		putPacket(4, PacketOutEntitySpawn.class);
		putPacket(5, PacketOutLivingEntityHealthChange.class);
		putPacket(6, PacketOutLivingPlayerDeath.class);
		putPacket(7, PacketOutOwnClientData.class);
		putPacket(8, PacketOutPlayerChangeAnimation.class);
	}

	@Override
	public Class<? extends Packet> get(Object key) {
		Class<? extends Packet> clazz = super.get(key);
		if (clazz == null) System.out.println("Packet " + key + " is null!");
		return clazz;
	}

	public void putPacket(int key, Class<? extends Packet> value) {
		put((short) key, value);
	}

	public int getPacketId(Packet packet) {

		return getPacketId(packet.getClass());
	}

	public int getPacketId(Class<? extends Packet> clazz) {
		short s = entrySet().stream().filter(entry -> entry.getValue().equals(clazz)).map(Entry::getKey).findAny().orElse((short) -1);
		if (s > 50 || s < 0) System.out.println("Packet " + clazz + " has an invalid value");
		return s;
	}
}
