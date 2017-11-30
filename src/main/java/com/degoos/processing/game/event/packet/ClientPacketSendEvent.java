package com.degoos.processing.game.event.packet;

import com.degoos.processing.engine.event.GEvent;
import com.degoos.processing.game.network.packet.Packet;

public class ClientPacketSendEvent extends GEvent {

	private Packet packet;

	public ClientPacketSendEvent(Packet packet) {
		this.packet = packet;

	}

	public Packet getPacket() {
		return packet;
	}

}
