package com.degoos.processing.game.entity;

import com.degoos.processing.engine.Processing;
import com.degoos.processing.engine.object.Animation;
import com.degoos.processing.game.controller.Controller;
import com.degoos.processing.game.enums.EnumCollideAction;
import com.degoos.processing.game.object.Area;
import com.flowpowered.math.vector.Vector2d;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class AuraSphere extends Projectile {

	private static Animation animation = new Animation("sphere", "png");
	private int launcher;

	public AuraSphere(Vector2d position, Controller controller, double damage, Vector2d direction, int launcher) {
		super(position, new Area(new Vector2d(-0.3, -0.3), new Vector2d(0.3, 0.3)), new Area(new Vector2d(-0.3, -0.3), new Vector2d(0.3, 0.3)), false, 0.005, true,
			controller, damage, direction);
		setTexture(animation);
		this.launcher = launcher;
	}

	public AuraSphere(int entityId, Vector2d position, Controller controller, double damage, Vector2d direction, int launcher) {
		super(entityId, position, new Area(new Vector2d(-0.3, -0.3), new Vector2d(0.3, 0.3)), new Area(new Vector2d(-0.3, -0.3), new Vector2d(0.3, 0.3)), false, 0.005,
			true, controller, damage, direction);
		this.launcher = launcher;
	}

	public AuraSphere(DataInputStream inputStream) throws IOException {
		super(inputStream);
		setTexture(animation);
		launcher = inputStream.readInt();
	}

	public AuraSphere(DataInputStream inputStream, Controller controller) throws IOException {
		super(inputStream, controller);
		setTexture(animation);
		launcher = inputStream.readInt();
	}


	public int getLauncher() {
		return launcher;
	}

	public void setLauncher(int launcher) {
		this.launcher = launcher;
	}

	@Override
	public EnumCollideAction collide(Entity entity) {
		if (launcher == entity.getEntityId()) return EnumCollideAction.PASS_THROUGH;
		return super.collide(entity);
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

	@Override
	public void save(DataOutputStream stream) throws IOException {
		super.save(stream);
		stream.writeInt(launcher);
	}
}
