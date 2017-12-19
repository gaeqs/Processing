package com.degoos.processing.game.network;

import com.degoos.processing.game.network.packet.Packet;
import com.flowpowered.math.vector.Vector2d;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
							DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
							String nick = dataInputStream.readUTF();
							if (serverClients.stream().anyMatch(c -> c.getNick().equalsIgnoreCase(nick))) {
								socket.close();
								return;
							}
							DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
							serverClients.add(new ServerClient(new Vector2d(12, 6), socket, dataInputStream, outputStream, nick));
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
