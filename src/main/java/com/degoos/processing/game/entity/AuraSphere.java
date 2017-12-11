package com.degoos.processing.game.entity;

import com.degoos.processing.engine.Processing;
import com.degoos.processing.engine.object.Animation;
import com.degoos.processing.game.Game;
import com.degoos.processing.game.controller.Controller;
import com.degoos.processing.game.enums.EnumCollideAction;
import com.degoos.processing.game.object.Area;
import com.flowpowered.math.vector.Vector2d;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class AuraSphere extends Projectile {

	private static Animation animation = new Animation("sphere", "png");
	private Entity launcher;
	private long launchMillis;

	public AuraSphere(Vector2d position, Controller controller, double damage, Vector2d direction, Entity launcher) {
		super(position, new Area(new Vector2d(-0.3, -0.3), new Vector2d(0.3, 0.3)), new Area(new Vector2d(-0.3, -0.3), new Vector2d(0.3, 0.3)), true, 0.02, true,
			controller, damage, direction);
		setTexture(animation);
		this.launcher = launcher;
		launchMillis = System.currentTimeMillis();
	}

	public AuraSphere(int entityId, Vector2d position, Controller controller, double damage, Vector2d direction, Entity launcher) {
		super(entityId, position, new Area(new Vector2d(-0.3, -0.3), new Vector2d(0.3, 0.3)), new Area(new Vector2d(-0.3, -0.3), new Vector2d(0.3, 0.3)), true, 0.02,
			true, controller, damage, direction);
		this.launcher = launcher;
		launchMillis = System.currentTimeMillis();
	}

	public AuraSphere(DataInputStream inputStream) throws IOException {
		super(inputStream);
		setTexture(animation);
		launcher = Game.getEntityManager().getEntity(inputStream.readInt()).orElse(null);
		launchMillis = inputStream.readLong();
	}

	public AuraSphere(DataInputStream inputStream, Controller controller) throws IOException {
		super(inputStream, controller);
		setTexture(animation);
		launcher = Game.getEntityManager().getEntity(inputStream.readInt()).orElse(null);
		launchMillis = inputStream.readLong();
	}


	public Entity getLauncher() {
		return launcher;
	}

	public void setLauncher(Entity launcher) {
		this.launcher = launcher;
	}

	public long getLaunchMillis() {
		return launchMillis;
	}

	public void setLaunchMillis(long launchMillis) {
		this.launchMillis = launchMillis;
	}

	@Override
	public EnumCollideAction collide(Entity entity) {
		if (launcher != null && launcher.equals(entity) /*&& launchMillis + 200 < System.currentTimeMillis()*/) return EnumCollideAction.PASS_THROUGH;
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
		stream.writeInt(launcher == null ? -1 : launcher.getId());
		stream.writeLong(launchMillis);
	}
}
