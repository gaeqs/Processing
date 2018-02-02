package com.degoos.processing.game.network;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.util.Validate;
import com.degoos.processing.game.Game;
import com.degoos.processing.game.controller.ClientController;
import com.degoos.processing.game.controller.Controller;
import com.degoos.processing.game.entity.Player;
import com.degoos.processing.game.event.packet.ClientPacketReceiveEvent;
import com.degoos.processing.game.event.packet.ClientPacketSendEvent;
import com.degoos.processing.game.network.packet.Packet;
import com.degoos.processing.game.network.packet.out.PacketOutEntityDelete;
import com.degoos.processing.game.network.packet.out.PacketOutEntitySpawn;
import com.degoos.processing.game.network.packet.out.PacketOutOwnClientData;
import com.flowpowered.math.vector.Vector2d;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerClient {

	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private Controller controller;
	private Player player;
	private String nick;

	public ServerClient(Vector2d position, Socket socket, InputStream inputStream, OutputStream outputStream, String nick) {
		Validate.notNull(nick, "Nick cannot be null!");
		this.socket = socket;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.nick = nick;
		this.controller = new ClientController(this);

		Game.getEntityManager().forEachEntities(entity -> sendPacket(new PacketOutEntitySpawn(entity)));

		this.player = new Player(position, controller, nick, true, false);
		this.player.teleportToSpawn();
		player.sendSpawnPacket();
		System.out.println("New player (" + nick + ") with id " + player.getEntityId());

		new Thread(() -> {
			try {
				while (!socket.isClosed()) {
					if (inputStream.available() == 0) continue;

					byte[] bytes = new byte[inputStream.available()];

					int i = inputStream.read(bytes);
					if (i != bytes.length) System.out.println("WARNING! " + i + " != " + bytes.length);

					ByteArrayDataInput in = ByteStreams.newDataInput(bytes);

					short s = (short) in.readInt();
					Class<? extends Packet> clazz = Game.getPacketMap().get(s);
					if (clazz == null) {
						System.out.println("CLASS" + s + " NULL!");
						disconnect();
						System.exit(0);
						return;
					}
					Packet packet = clazz.getConstructor(DataInput.class).newInstance(in);
					Engine.getEventManager().callEvent(new ClientPacketReceiveEvent(packet));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				disconnect();
			}
		}).start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sendPacket(new PacketOutOwnClientData(player.getEntityId(), player.getPosition(), nick));
	}

	public Socket getSocket() {
		return socket;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public OutputStream getOutputStream() {
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
		sendPacket(packet, false);
	}

	private void sendPacket(Packet packet, boolean secondTry) {
		if (!secondTry) Engine.getEventManager().callEvent(new ClientPacketSendEvent(packet));
		try {
			ByteArrayDataOutput output = ByteStreams.newDataOutput();
			output.writeInt(Game.getPacketMap().getPacketId(packet));
			packet.write(output);
			outputStream.write(output.toByteArray());
		} catch (Exception ex) {
			ex.printStackTrace();
			if (secondTry) disconnect();
			else sendPacket(packet, true);
		}
	}


	public void disconnect() {
		if (!socket.isClosed()) try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Game.getGameServer().deleteServerClient(this);
		Packet packet = new PacketOutEntityDelete(player.getEntityId());
		Game.getGameServer().getServerClients().forEach(client -> client.sendPacket(packet));
		player.delete();
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
