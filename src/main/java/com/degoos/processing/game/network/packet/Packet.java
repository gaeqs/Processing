package com.degoos.processing.game.network.packet;

import java.io.DataOutput;

public abstract class Packet {

	public abstract void write(DataOutput outputStream);

}
