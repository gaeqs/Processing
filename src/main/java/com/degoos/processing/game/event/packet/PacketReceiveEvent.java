package com.degoos.processing.game.event.packet;

import com.degoos.processing.engine.event.GEvent;
import com.degoos.processing.game.network.ServerClient;
import com.degoos.processing.game.network.packet.Packet;

public class PacketReceiveEvent extends GEvent {

	private Packet packet;
	private ServerClient serverClient;

	public PacketReceiveEvent(Packet packet, ServerClient serverClient) {
		this.packet = packet;
		this.serverClient = serverClient;
	}

	public Packet getPacket() {
		return packet;
	}

	public ServerClient getServerClient() {
		return serverClient;
	}
}
