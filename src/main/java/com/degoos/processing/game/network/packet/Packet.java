package com.degoos.processing.game.network.packet;

import java.io.DataOutputStream;

public abstract class Packet {

	public abstract void write(DataOutputStream outputStream);

}
