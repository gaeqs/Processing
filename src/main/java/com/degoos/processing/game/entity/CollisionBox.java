package com.degoos.processing.game.entity;

import com.degoos.processing.game.object.Area;
import com.degoos.processing.game.object.Level;
import com.flowpowered.math.vector.Vector2d;
import java.io.DataInputStream;
import java.io.IOException;

public class CollisionBox extends SavableEntity {

	public CollisionBox(Vector2d position, Area relativeCollisionBox, Level level) {
		super(position, relativeCollisionBox, new Area(new Vector2d(), new Vector2d()), true, 0, null, level);
	}

	public CollisionBox(Area box, Level level) {
		super(box.getMin(), new Area(new Vector2d(), box.getMax().sub(box.getMin())), new Area(new Vector2d(), new Vector2d()), true, 0, null, level);
	}


	public CollisionBox(DataInputStream inputStream, Level level) throws IOException {
		super(inputStream, level);
	}

}
