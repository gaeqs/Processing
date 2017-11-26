package com.degoos.processing.game.object;

import com.degoos.processing.engine.util.Validate;
import com.flowpowered.math.vector.Vector2d;
import java.awt.geom.Rectangle2D.Double;

public class Area {

	private Vector2d min;
	private Vector2d max;
	protected Double rectangle;

	public Area(Vector2d min, Vector2d max) {
		this.min = min;
		this.max = max;
		recalculate();
	}

	public Vector2d getMin() {
		return min;
	}

	public void setMin(Vector2d min) {
		Validate.notNull(min, "Min position cannot be null!");
		this.min = min;
		recalculate();
	}

	public Vector2d getMax() {
		return max;
	}

	public void setMax(Vector2d max) {
		Validate.notNull(max, "Max position cannot be null!");
		this.max = max;
		recalculate();
	}

	public Vector2d getMinMax() {
		return new Vector2d(min.getX(), max.getY());
	}

	public Vector2d getMaxMin() {
		return new Vector2d(max.getX(), min.getY());
	}


	public boolean isInside(Vector2d vector2d) {
		return isInsideX(vector2d.getX()) && isInsideY(vector2d.getY());
	}

	public boolean isInsideX(double x) {
		return x > min.getX() && x < max.getX();
	}

	public boolean isInsideY(double y) {
		return y > min.getY() && y < max.getY();
	}


	public boolean goThrough(Vector2d p1, Vector2d p2) {
		Area through = new Area(p1, p2);
		return rectangle.intersects(through.rectangle);
	}

	public boolean collide(Area area) {
		return rectangle.intersects(area.rectangle);
	}

	private void recalculate() {
		Vector2d oMin = this.min;
		Vector2d oMax = this.max;
		min = new Vector2d(Math.min(oMin.getX(), oMax.getX()), Math.min(oMin.getY(), oMax.getY()));
		max = new Vector2d(Math.max(oMin.getX(), oMax.getX()), Math.max(oMin.getY(), oMax.getY()));
		rectangle = new Double(min.getX(), min.getY(), max.getX() - min.getX(), max.getY() - min.getY());
	}
}
