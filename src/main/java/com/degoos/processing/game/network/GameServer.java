package com.degoos.processing.game.network;

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

	public GameServer() {
		serverClients = new HashSet<>();
		try {
			serverSocket = new ServerSocket(22222);
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
							serverClients.add(new ServerClient(socket, dataInputStream, new DataOutputStream(socket.getOutputStream()), null, nick));
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}).start();
				} catch (Exception ex) {
					ex.printStackTrace();
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
}
