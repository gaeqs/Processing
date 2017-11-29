package com.degoos.processing.game.network.packet.in;

import com.degoos.processing.engine.enums.EnumKeyboardKey;
import com.degoos.processing.game.network.packet.Packet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketInPressKey extends Packet {

	private EnumKeyboardKey keyboardKey;

	public PacketInPressKey(EnumKeyboardKey keyboardKey) {
		this.keyboardKey = keyboardKey;
	}

	public PacketInPressKey(DataInputStream stream) {
		try {
			keyboardKey = EnumKeyboardKey.getById(stream.readInt()).orElse(EnumKeyboardKey.UNDEFINED);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public EnumKeyboardKey getKeyboardKey() {
		return keyboardKey;
	}

	@Override
	public void write(DataOutputStream outputStream) {
		try {
			outputStream.writeInt(keyboardKey.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
