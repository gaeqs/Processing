package com.degoos.processing.game.entity;

import com.degoos.processing.game.Game;
import com.degoos.processing.game.controller.Controller;
import com.degoos.processing.game.object.Area;
import com.degoos.processing.game.object.Level;
import com.flowpowered.math.vector.Vector2d;
import java.io.DataInput;
import java.io.IOException;

public class SavableEntity extends Entity {

	private Level level;

	public SavableEntity(Vector2d position, Area relativeCollisionBox, Area relativeDisplayArea, boolean tangible, double velocity, Controller controller, Level level) {
		super(position, relativeCollisionBox, relativeDisplayArea, tangible, velocity, false, controller);
		this.level = level;
		level.addEntity(this);
	}

	public SavableEntity(int id, Vector2d position, Area relativeCollisionBox, Area relativeDisplayArea, boolean tangible, double velocity, Controller controller,
		Level level) {
		super(id, position, relativeCollisionBox, relativeDisplayArea, tangible, velocity, false, controller);
		this.level = level;
		level.addEntity(this);
	}

	public SavableEntity(DataInput inputStream, Level level) throws IOException {
		super(inputStream);
		this.level = level;
		level.addEntity(this);
	}

	public SavableEntity(DataInput inputStream) throws IOException {
		super(inputStream);
		this.level = Game.getLevel();
		level.addEntity(this);
	}


	public Level getLevel() {
		return level;
	}

	@Override
	public void delete() {
		super.delete();
		level.getLevelEntities().remove(this);
	}
}
