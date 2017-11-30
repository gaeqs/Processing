package com.degoos.processing.game.network;

import com.degoos.processing.game.MapLoader;
import com.flowpowered.math.vector.Vector2d;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ServerConnection {

	private Socket socket;
	private String nick;
	private boolean loaded;

	public ServerConnection(String nick, String ip) {
		this.nick = nick;
		this.loaded = false;
		try {
			String[] sl = ip.split(":");
			socket = new Socket(sl[0], sl.length == 1 || !isInteger(sl[1]) ? 22222 : Integer.parseInt(sl[1]));
			DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
			DataInputStream inputStream = new DataInputStream(socket.getInputStream());
			outputStream.writeUTF(nick);
			if (!socket.isConnected()) {
				System.exit(0);
				return;
			}
			MapLoader.load();

			while (socket.isConnected()){
				if(inputStream.available() == 0) continue;

			}

			loaded = true;
		} catch (Exception ex) {
			System.out.println("Server not found!");
		}
	}

	public boolean isLoaded() {
		return loaded;
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
