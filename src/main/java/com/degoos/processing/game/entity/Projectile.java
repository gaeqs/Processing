package com.degoos.processing.game.entity;

import com.degoos.processing.game.Game;
import com.degoos.processing.game.controller.Controller;
import com.degoos.processing.game.enums.EnumCollideAction;
import com.degoos.processing.game.object.Area;
import com.degoos.processing.game.util.StreamUtils;
import com.flowpowered.math.vector.Vector2d;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Projectile extends Entity {

	private double damage;
	private Vector2d direction;

	public Projectile(Vector2d position, Area relativeCollisionBox, Area relativeDisplayArea, boolean tangible, double velocity, boolean canMove, Controller controller,
		double damage, Vector2d direction) {
		super(position, relativeCollisionBox, relativeDisplayArea, tangible, velocity, canMove, controller);
		setTickPriority(0);
		this.damage = damage;
		setDirection(direction);
	}

	public Projectile(int entityId, Vector2d position, Area relativeCollisionBox, Area relativeDisplayArea, boolean tangible, double velocity, boolean canMove,
		Controller controller, double damage, Vector2d direction) {
		super(entityId, position, relativeCollisionBox, relativeDisplayArea, tangible, velocity, canMove, controller);
		setTickPriority(0);
		this.damage = damage;
		setDirection(direction);
	}

	public Projectile(DataInputStream inputStream) throws IOException {
		super(inputStream);
		setTickPriority(0);
		this.damage = inputStream.readDouble();
		setDirection(new Vector2d(inputStream.readDouble(), inputStream.readDouble()));
	}

	public Projectile(DataInputStream inputStream, Controller controller) throws IOException {
		super(inputStream, controller);
		setTickPriority(0);
		this.damage = inputStream.readDouble();
		setDirection(new Vector2d(inputStream.readDouble(), inputStream.readDouble()));
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public Vector2d getDirection() {
		return direction;
	}

	public void setDirection(Vector2d direction) {
		this.direction = direction;
	}

	@Override
	public void save(DataOutputStream stream) throws IOException {
		super.save(stream);
		stream.writeDouble(damage);
		StreamUtils.writeVector(stream, direction);
	}

	@Override
	public EnumCollideAction collide(Entity entity) {
		if (!entity.isTangible()) return EnumCollideAction.PASS_THROUGH;
		if (entity instanceof Teleport) return EnumCollideAction.PASS_THROUGH;
		if (entity instanceof LivingEntity) ((LivingEntity) entity).addHealth(-damage);
		delete();
		return EnumCollideAction.PASS_THROUGH;
	}

	@Override
	public void onTick(long dif) {
		if (!Game.isServer()) {
			super.onTick(dif);
			return;
		}
		double velX = direction.getX() * getVelocity() * dif;
		double velY = direction.getY() * getVelocity() * dif;
		move(velX, velY);
		super.onTick(dif);
	}
}
