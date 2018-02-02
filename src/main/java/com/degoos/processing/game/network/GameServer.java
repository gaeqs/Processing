package com.degoos.processing.game.network;

import com.degoos.processing.game.network.packet.Packet;
import com.flowpowered.math.vector.Vector2d;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class GameServer {

	private ServerSocket serverSocket;
	private Set<ServerClient> serverClients;
	private boolean shuttingDown;

	public GameServer() {
		serverClients = new HashSet<>();
		shuttingDown = false;
		try {
			serverSocket = new ServerSocket(22222);
			serverSocket.setSoTimeout(0);
			startAcceptThread();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void startAcceptThread() {
		new Thread(() -> {
			while (!serverSocket.isClosed()) {
				try {
					Socket socket = serverSocket.accept();
					new Thread(() -> {
						try {
							InputStream inputStream = socket.getInputStream();
							while (!socket.isClosed()) {
								if (inputStream.available() == 0) continue;

								byte[] bytes = new byte[inputStream.available()];

								int i = inputStream.read(bytes);
								if (i != bytes.length) System.out.println("WARNING! " + i + " != " + bytes.length);

								ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
								String nick = in.readUTF();
								if (serverClients.stream().anyMatch(c -> c.getNick().equalsIgnoreCase(nick))) {
									socket.close();
									return;
								}
								serverClients.add(new ServerClient(new Vector2d(12, 6), socket, inputStream, socket.getOutputStream(), nick));
								break;
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}).start();
				} catch (Exception ex) {
					if (!shuttingDown) ex.printStackTrace();
				}
			}
		}).start();
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public Set<ServerClient> getServerClients() {
		return new HashSet<>(serverClients);
	}

	public void deleteServerClient(ServerClient client) {
		serverClients.remove(client);
	}

	public void sendPacket(Packet packet) {
		serverClients.forEach(client -> client.sendPacket(packet));
	}

	public void disconnect() {
		try {
			shuttingDown = true;
			if (!serverSocket.isClosed()) serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
