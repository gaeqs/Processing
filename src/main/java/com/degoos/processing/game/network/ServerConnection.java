package com.degoos.processing.game.network;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.game.Game;
import com.degoos.processing.game.MapLoader;
import com.degoos.processing.game.event.packet.ClientPacketReceiveEvent;
import com.degoos.processing.game.event.packet.ClientPacketSendEvent;
import com.degoos.processing.game.listener.ClientListener;
import com.degoos.processing.game.network.packet.Packet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerConnection {

	private Socket socket;
	private String nick;
	private boolean loaded;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	private boolean shuttingDown;
	private long lastPing;

	public ServerConnection(String nick, String ip) {
		this.nick = nick;
		this.loaded = false;
		this.shuttingDown = false;
		new Thread(() -> {
			try {
				Engine.getEventManager().registerListener(new ClientListener());
				String[] sl = ip.split(":");
				socket = new Socket(sl[0], sl.length == 1 || !isInteger(sl[1]) ? 22222 : Integer.parseInt(sl[1]));
				outputStream = new DataOutputStream(socket.getOutputStream());
				inputStream = new DataInputStream(socket.getInputStream());
				outputStream.writeUTF(nick);
				if (!socket.isConnected()) {
					System.exit(0);
					return;
				}
				MapLoader.load();
				loaded = true;
				Game.getMenu().deleteAll();
				new Thread(() -> {
					try {
						lastPing = System.currentTimeMillis();
						while (!socket.isClosed()) {
							if (System.currentTimeMillis() - lastPing > 1000) {
								outputStream.writeUTF("c");
								lastPing = System.currentTimeMillis();
							}
							if (inputStream.available() == 0) continue;
							String s = inputStream.readUTF();
							if (s.equals("c")) continue;
							Class<?> clazz = Class.forName(s);
							Packet packet = (Packet) clazz.getConstructor(DataInputStream.class).newInstance(inputStream);
							Engine.getEventManager().callEvent(new ClientPacketReceiveEvent(packet));
						}
					} catch (Exception ex) {
						if (shuttingDown) return;
						System.exit(0);
					}
				}).start();
			} catch (Exception ex) {
				System.out.println("Server not found!");
				Game.setLoading(false);
				Game.getMenu().setAllVisible(true);
			}
		}).start();

	}

	public boolean isLoaded() {
		return loaded;
	}

	public void sendPacket(Packet packet) {
		if (!loaded) return;
		Engine.getEventManager().callEvent(new ClientPacketSendEvent(packet));
		try {
			outputStream.writeUTF(packet.getClass().getName());
			packet.write(outputStream);
		} catch (Exception ex) {
			disconnect();
		}
	}

	public void disconnect() {
		shuttingDown = true;
		try {
			if (!socket.isClosed()) socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected boolean isInteger(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

}
