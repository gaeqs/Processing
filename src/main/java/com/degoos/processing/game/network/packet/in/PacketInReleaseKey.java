package com.degoos.processing.game.network.packet.in;

import com.degoos.processing.engine.enums.EnumKeyboardKey;
import com.degoos.processing.game.network.packet.Packet;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class PacketInReleaseKey extends Packet {

	private EnumKeyboardKey keyboardKey;

	public PacketInReleaseKey(EnumKeyboardKey keyboardKey) {
		this.keyboardKey = keyboardKey;
	}

	public PacketInReleaseKey(DataInput stream) {
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
	public void write(DataOutput outputStream) {
		try {
			outputStream.writeInt(keyboardKey.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
