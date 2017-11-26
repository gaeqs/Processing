package com.degoos.processing.game.object;

import com.degoos.processing.engine.util.Validate;
import com.degoos.processing.game.enums.EnumCollisionFace;
import com.flowpowered.math.vector.Vector2d;
import java.awt.geom.Line2D;

public class LinealArea {

	private Vector2d pos1, pos2;
	protected Line2D line;

	public LinealArea(Vector2d pos1, Vector2d pos2) {
		Validate.notNull(pos1, "Pos 1 cannot be null!");
		Validate.notNull(pos2, "Pos 2 cannot be null!");
		this.pos1 = pos1;
		this.pos2 = pos2;
		recalculate();
	}

	public Vector2d getPos1() {
		return pos1;
	}

	public void setPos1(Vector2d pos1) {
		Validate.notNull(pos1, "Pos 1 cannot be null!");
		this.pos1 = pos1;
		recalculate();
	}

	public Vector2d getPos2() {
		return pos2;
	}

	public void setPos2(Vector2d pos2) {
		Validate.notNull(pos2, "Pos 2 cannot be null!");
		this.pos2 = pos2;
		recalculate();
	}

	public boolean collide(LinealArea l) {
		return line.intersectsLine(l.line);
	}

	public boolean collide(Area area) {
		return area.rectangle.intersectsLine(line);
	}

	public Collision getFirstCollisionPoint(Area area) {
		if (area.isInside(pos1)) return new Collision(EnumCollisionFace.UP, pos1);
		boolean movingLeft = pos1.getX() > pos2.getX();
		boolean movingDown = pos1.getY() > pos2.getY();
		if (pos1.getX() != pos2.getX()) {
			if (pos1.getY() == pos2.getY()) {
				double x = movingLeft ? area.getMax().getX() : area.getMin().getX();
				if (pos1.getY() > area.getMin().getY() && pos1.getY() < area.getMax().getY())
					return new Collision(movingLeft ? EnumCollisionFace.LEFT : EnumCollisionFace.RIGHT, new Vector2d(x, pos1.getY()));
			}
			double m = (pos1.getY() - pos2.getY()) / (pos1.getX() - pos2.getX());
			double x = movingLeft ? area.getMax().getX() : area.getMin().getX();
			double n = pos1.getY() - (m * pos1.getX());
			double y = m * x + n;
			if (y > area.getMin().getY() && y < area.getMax().getY())
				return new Collision(movingLeft ? EnumCollisionFace.LEFT : EnumCollisionFace.RIGHT, new Vector2d(x, y));
		}
		if (pos1.getY() != pos2.getY()) {
			if (pos1.getX() == pos2.getX()) {
				double y = movingDown ? area.getMax().getY() : area.getMin().getY();
				if (pos1.getX() > area.getMin().getX() && pos1.getX() < area.getMax().getX())
					return new Collision(movingDown ? EnumCollisionFace.DOWN : EnumCollisionFace.UP, new Vector2d(pos1.getX(), y));
			} else {
				double m = (pos1.getY() - pos2.getY()) / (pos1.getX() - pos2.getX());
				double y = movingDown ? area.getMax().getY() : area.getMin().getY();
				double n = pos1.getY() - (m * pos1.getX());
				double x = (y - n) / m;
				if (x > area.getMin().getX() && x < area.getMax().getX())
					return new Collision(movingDown ? EnumCollisionFace.DOWN : EnumCollisionFace.UP, new Vector2d(x, y));
			}
		}
		return null;
	}

	private void recalculate() {
		line = new Line2D.Double(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY());
	}
}
