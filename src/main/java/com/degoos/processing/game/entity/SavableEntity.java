package com.degoos.processing.game.entity;

import com.degoos.processing.game.controller.Controller;
import com.degoos.processing.game.object.Area;
import com.degoos.processing.game.object.Level;
import com.degoos.processing.game.util.StreamUtils;
import com.flowpowered.math.vector.Vector2d;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SavableEntity extends Entity {

	private Level level;

	public SavableEntity(Vector2d position, Area relativeCollisionBox, Area relativeDisplayArea, boolean tangible, double velocity, Controller controller, Level level) {
		super(position, relativeCollisionBox, relativeDisplayArea, tangible, velocity, controller);
		this.level = level;
		level.addEntity(this);
	}

	public SavableEntity(DataInputStream inputStream, Level level) throws IOException {
		super(new Vector2d(inputStream.readDouble(), inputStream.readDouble()), new Area(new Vector2d(inputStream.readDouble(), inputStream
			.readDouble()), new Vector2d(inputStream.readDouble(), inputStream.readDouble())), new Area(new Vector2d(inputStream.readDouble(), inputStream
			.readDouble()), new Vector2d(inputStream.readDouble(), inputStream.readDouble())), inputStream.readBoolean(), inputStream.readDouble(), null);
		this.level = level;
		level.addEntity(this);
	}

	public Level getLevel() {
		return level;
	}

	public void save(DataOutputStream stream) throws IOException {
		StreamUtils.writeVector(stream, getPosition());
		StreamUtils.writeArea(stream, getRelativeCollisionBox());
		StreamUtils.writeArea(stream, getRelativeDisplayArea());
		stream.writeBoolean(isTangible());
		stream.writeDouble(getVelocity());
	}

	@Override
	public void delete() {
		super.delete();
		level.getLevelEntities().remove(this);
	}
}
