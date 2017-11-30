package com.degoos.processing.game.network;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.util.Validate;
import com.degoos.processing.game.controller.ClientController;
import com.degoos.processing.game.controller.Controller;
import com.degoos.processing.game.entity.Player;
import com.degoos.processing.game.event.packet.PacketReceiveEvent;
import com.degoos.processing.game.event.packet.PacketSendEvent;
import com.degoos.processing.game.network.packet.Packet;
import com.flowpowered.math.vector.Vector2d;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ServerClient {

	private Socket socket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private Controller controller;
	private Player player;
	private String nick;

	public ServerClient(Vector2d position, Socket socket, DataInputStream inputStream, DataOutputStream outputStream, Controller controller, String nick) {
		Validate.notNull(nick, "Nick cannot be null!");
		this.socket = socket;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.controller = controller;
		this.nick = nick;
		this.controller = new ClientController(this);
		this.player = new Player(position, controller);

		try {
			while (socket.isConnected()) {
				if(inputStream.available() == 0) continue;
				Class<?> clazz = Class.forName(inputStream.readUTF());
				if (!clazz.isAssignableFrom(Packet.class)) {
					socket.close();
					return;
				}
				Packet packet = (Packet) clazz.getConstructor(DataInputStream.class).newInstance(inputStream);
				Engine.getEventManager().callEvent(new PacketReceiveEvent(packet, this));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			player.delete();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public DataInputStream getInputStream() {
		return inputStream;
	}

	public DataOutputStream getOutputStream() {
		return outputStream;
	}

	public Controller getController() {
		return controller;
	}

	public String getNick() {
		return nick;
	}

	public Player getPlayer() {
		return player;
	}

	public void sendPacket(Packet packet) {
		Engine.getEventManager().callEvent(new PacketSendEvent(packet, this));
		try {
			outputStream.writeUTF(packet.getClass().getName());
			packet.write(outputStream);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ServerClient serverClient = (ServerClient) o;

		return nick.equals(serverClient.nick);
	}

	@Override
	public int hashCode() {
		return nick.hashCode();
	}
}
