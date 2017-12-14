package com.degoos.processing.game.object;

import com.degoos.processing.game.entity.Entity;
import com.flowpowered.math.vector.Vector2d;

public class Camera {

	private Vector2d position;
	private double xRadius, yRadius;

	public Camera(Vector2d position, double xRadius, double yRadius) {
		this.position = position;
		this.xRadius = xRadius;
		this.yRadius = yRadius;
	}

	public Vector2d getPosition() {
		return position;
	}

	public void setPosition(Vector2d position) {
		this.position = position;
	}

	public double getXRadius() {
		return xRadius;
	}

	public void setXRadius(double xRadius) {
		this.xRadius = xRadius;
	}

	public double getYRadius() {
		return yRadius;
	}

	public void setYRadius(double yRadius) {
		this.yRadius = yRadius;
	}

	public void move(Vector2d vector2d) {
		move(vector2d.getX(), vector2d.getY());
	}

	public void move(double x, double y) {
		position = position.add(x, y);
	}

	public boolean isVisible(Entity entity) {
		if (entity == null) return false;
		Vector2d min = entity.getCurrentDisplayArea().getMin();
		Vector2d max = entity.getCurrentDisplayArea().getMax();
		return isPointVisible(min) || isPointVisible(new Vector2d(min.getX(), max.getY())) || isPointVisible(max) || isPointVisible(new Vector2d(max.getX(), min.getY
			()));
	}

	public boolean isPointVisible(Vector2d point) {
		return point.getX() > position.getX() - xRadius && point.getX() < position.getX() + xRadius && point.getY() > position.getY() - yRadius &&
			point.getY() < position.getY() + yRadius;
	}
}
