package com.degoos.processing.game.entity;

import com.degoos.processing.engine.Processing;
import com.degoos.processing.engine.object.Animation;
import com.degoos.processing.game.controller.Controller;
import com.degoos.processing.game.object.Area;
import com.flowpowered.math.vector.Vector2d;
import java.io.DataInputStream;
import java.io.IOException;

public class AuraSphere extends Projectile {

	private static Animation animation = new Animation("sphere", "png");

	public AuraSphere(Vector2d position, Controller controller, double damage, Vector2d direction) {
		super(position, new Area(new Vector2d(-0.3, -0.3), new Vector2d(0.3, 0.3)), new Area(new Vector2d(-0.3, -0.3), new Vector2d(0.3, 0.3)), true, 0.02, true,
			controller, damage, direction);
		setTexture(animation);
	}

	public AuraSphere(int entityId, Vector2d position, Controller controller, double damage, Vector2d direction) {
		super(entityId, position, new Area(new Vector2d(-0.3, -0.3), new Vector2d(0.3, 0.3)), new Area(new Vector2d(-0.3, -0.3), new Vector2d(0.3, 0.3)), true, 0.02,
			true, controller, damage, direction);
		setTexture(animation);
	}

	public AuraSphere(DataInputStream inputStream) throws IOException {
		super(inputStream);
		setTexture(animation);
	}

	public AuraSphere(DataInputStream inputStream, Controller controller) throws IOException {
		super(inputStream, controller);
		setTexture(animation);
	}

	@Override
	public void setDirection(Vector2d direction) {
		super.setDirection(direction);
		if (direction.getX() == 0) {
			setRotation(direction.getY() > 0 ? Processing.PI : 0);
		} else {
			double atan = Math.atan2(direction.getY(), direction.getX());
			setRotation((-(float) atan) - Processing.HALF_PI);
		}
	}
}
