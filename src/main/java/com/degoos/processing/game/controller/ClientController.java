package com.degoos.processing.game.controller;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.event.Listener;
import com.degoos.processing.game.entity.Entity;
import com.degoos.processing.game.event.packet.PacketReceiveEvent;
import com.degoos.processing.game.network.ServerClient;
import com.degoos.processing.game.network.packet.in.PacketInPressKey;
import com.degoos.processing.game.network.packet.in.PacketInReleaseKey;

public class ClientController implements Controller {

	public static boolean up, down, left, right, control;
	private ServerClient client;

	public ClientController(ServerClient client) {
		Engine.getEventManager().registerListener(this);
		this.client = client;
	}

	@Listener
	public void onKeyPressed(PacketReceiveEvent event) {
		if (!client.equals(event.getServerClient())) return;
		if (!(event.getPacket() instanceof PacketInPressKey)) return;
		switch (((PacketInPressKey) event.getPacket()).getKeyboardKey()) {
			case UP:
				up = true;
				break;
			case LEFT:
				left = true;
				break;
			case DOWN:
				down = true;
				break;
			case RIGHT:
				right = true;
				break;
			case CONTROL:
				control = true;
				break;
		}
	}

	@Listener
	public void onKeyReleased(PacketReceiveEvent event) {
		if (!client.equals(event.getServerClient())) return;
		if (!(event.getPacket() instanceof PacketInReleaseKey)) return;
		switch (((PacketInReleaseKey) event.getPacket()).getKeyboardKey()) {
			case UP:
				up = false;
				break;
			case LEFT:
				left = false;
				break;
			case DOWN:
				down = false;
				break;
			case RIGHT:
				right = false;
				break;
			case CONTROL:
				control = false;
				break;
		}
	}

	@Override
	public void onTick(long dif, Entity entity) {
		entity.triggerMove(up, down, left, right, dif);
		if (control) entity.setVelocity(0.007D);
		else entity.setVelocity(0.004D);
	}
}
