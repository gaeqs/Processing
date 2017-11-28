package com.degoos.processing.game.object;

import com.degoos.processing.engine.util.Validate;
import com.degoos.processing.game.enums.EnumCollisionFace;
import com.flowpowered.math.vector.Vector2d;
import java.awt.geom.Line2D;

public class LinealArea {

	private Vector2d pos1, pos2;
	protected Line2D.Double line;

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
		return getFirstCollisionPoint(area, false);
	}

	public Collision getFirstCollisionPoint(Area area, boolean debug) {
		if (area.isInside(pos1)) return new Collision(EnumCollisionFace.UP, pos1);
		boolean movingLeft = pos1.getX() > pos2.getX();
		boolean movingDown = pos1.getY() > pos2.getY();

		/*Line2D.Double vertical = movingDown ? new Line2D.Double(area.getMin().getX(), area.getMax().getY(), area.getMax().getX(), area.getMax().getY())
		                                    : new Line2D.Double(area.getMin().getX(), area.getMin().getY(), area.getMax().getX(), area.getMin().getY());
		Line2D.Double horizontal = movingLeft ? new Line2D.Double(area.getMax().getX(), area.getMin().getY(), area.getMax().getX(), area.getMax().getY())
		                                      : new Line2D.Double(area.getMin().getX(), area.getMin().getY(), area.getMin().getX(), area.getMax().getY());
		if (vertical.intersectsLine(line)) return new Collision(movingDown ? EnumCollisionFace.DOWN : EnumCollisionFace.UP, LineUtils.getIntersection(vertical, line));
		if (horizontal.intersectsLine(line))
			return new Collision(movingLeft ? EnumCollisionFace.LEFT : EnumCollisionFace.RIGHT, LineUtils.getIntersection(horizontal, line));*/

		if (pos1.getX() != pos2.getX()) {
			if (debug) System.out.println("X");
			if (pos1.getY() == pos2.getY()) {
				double x = movingLeft ? area.getMax().getX() : area.getMin().getX();
				if (pos1.getY() > area.getMin().getY() && pos1.getY() < area.getMax().getY())
					return new Collision(movingLeft ? EnumCollisionFace.LEFT : EnumCollisionFace.RIGHT, new Vector2d(x, pos1.getY()));
			}
			double m = (pos1.getY() - pos2.getY()) / (pos1.getX() - pos2.getX());
			double x = movingLeft ? area.getMax().getX() : area.getMin().getX();
			double n = pos1.getY() - (m * pos1.getX());
			double y = m * x + n;
			if (debug) {
				System.out.println(x + " -> " + y);
				System.out.println(area.getMin().getY() + " < " + y + " > " + area.getMax().getY());
			}
			if (y > area.getMin().getY() && y < area.getMax().getY())
				return new Collision(movingLeft ? EnumCollisionFace.LEFT : EnumCollisionFace.RIGHT, new Vector2d(x, y));
		}
		if (pos1.getY() != pos2.getY()) {
			if (debug) System.out.println("Y");
			if (pos1.getX() == pos2.getX()) {
				double y = movingDown ? area.getMax().getY() : area.getMin().getY();
				if (pos1.getX() > area.getMin().getX() && pos1.getX() < area.getMax().getX())
					return new Collision(movingDown ? EnumCollisionFace.DOWN : EnumCollisionFace.UP, new Vector2d(pos1.getX(), y));
			} else {
				double m = (pos1.getX() - pos2.getX()) / (pos1.getY() - pos2.getY());
				double y = movingDown ? area.getMax().getY() : area.getMin().getY();
				double n = pos1.getX() - (m * pos1.getY());
				double x = m * y + n;
				if (debug) {
					System.out.println(y + " -> " + x);
					System.out.println(area.getMin().getX() + " < " + x + " > " + area.getMax().getX());
				}
				if (x > area.getMin().getX() && x < area.getMax().getX())
					return new Collision(movingDown ? EnumCollisionFace.DOWN : EnumCollisionFace.UP, new Vector2d(x, y));
			}
		}
		if (debug) System.out.println("NONE");
		return null;
	}

	private void recalculate() {
		line = new Line2D.Double(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY());
	}
}
