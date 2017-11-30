package com.degoos.processing.game.listener;

import com.degoos.processing.engine.event.Listener;
import com.degoos.processing.game.Game;
import com.degoos.processing.game.entity.Player;
import com.degoos.processing.game.event.packet.ClientPacketReceiveEvent;
import com.degoos.processing.game.network.packet.out.PacketOutOwnClientData;

public class ClientListener {

	@Listener
	public void onPacketReceive(ClientPacketReceiveEvent event) {
		if (event.getPacket() instanceof PacketOutOwnClientData) {
			Game.setPlayer(new Player(((PacketOutOwnClientData) event.getPacket()).getEntityId(), ((PacketOutOwnClientData) event.getPacket()).getPosition(), null));
		}
	}

}
