package com.degoos.processing.game.entity;

import com.degoos.processing.game.object.Area;
import com.degoos.processing.game.object.Level;
import com.flowpowered.math.vector.Vector2d;
import java.io.DataInputStream;
import java.io.IOException;

public class Spawn extends SavableEntity {

	public Spawn(Vector2d position, Level level) {
		super(position, new Area(new Vector2d(-0.25, -0.25), new Vector2d(0.25, 0.25)), Area.empty(), false, 0, null, level);
	}

	public Spawn(int id, Vector2d position, Level level) {
		super(id, position, new Area(new Vector2d(-0.25, -0.25), new Vector2d(0.25, 0.25)), Area.empty(), false, 0, null, level);
	}

	public Spawn(DataInputStream inputStream, Level level) throws IOException {
		super(inputStream, level);
	}

	public Spawn(DataInputStream inputStream) throws IOException {
		super(inputStream);
	}
}
