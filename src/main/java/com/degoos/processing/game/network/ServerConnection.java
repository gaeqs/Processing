package com.degoos.processing.game.network;

import java.io.DataOutputStream;
import java.net.Socket;

public class ServerConnection {

	private Socket socket;
	private String nick;

	public ServerConnection(String nick, String ip) {
		this.nick = nick;
		try {
			String[] sl = ip.split(":");
			socket = new Socket(sl[0], sl.length == 1 || !isInteger(sl[1]) ? 22222 : Integer.parseInt(sl[1]));
			DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
			outputStream.writeUTF(nick);
		} catch (Exception ex) {
			ex.printStackTrace();
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
