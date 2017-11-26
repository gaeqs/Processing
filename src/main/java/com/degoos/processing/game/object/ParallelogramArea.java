package com.degoos.processing.game.object;

import com.degoos.processing.engine.util.Validate;
import com.flowpowered.math.vector.Vector2d;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;

public class ParallelogramArea {

	private Vector2d p1, p2, p3, p4;
	protected Path2D parallelogram;

	public ParallelogramArea(Vector2d p1, Vector2d p2, Vector2d p3, Vector2d p4) {
		Validate.notNull(p1, "Pos 1 cannot be null!");
		Validate.notNull(p2, "Pos 2 cannot be null!");
		Validate.notNull(p3, "Pos 3 cannot be null!");
		Validate.notNull(p4, "Pos 4 cannot be null!");
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
		recalculate();
	}

	public Vector2d getP1() {
		return p1;
	}

	public void setP1(Vector2d p1) {
		Validate.notNull(p1, "Pos 1 cannot be null!");
		this.p1 = p1;
	}

	public Vector2d getP2() {
		return p2;
	}

	public void setP2(Vector2d p2) {
		Validate.notNull(p2, "Pos 2 cannot be null!");
		this.p2 = p2;
	}

	public Vector2d getP3() {
		return p3;
	}

	public void setP3(Vector2d p3) {
		Validate.notNull(p3, "Pos 3 cannot be null!");
		this.p3 = p3;
	}

	public Vector2d getP4() {
		return p4;
	}

	public void setP4(Vector2d p4) {
		Validate.notNull(p4, "Pos 4 cannot be null!");
		this.p4 = p4;
	}

	public boolean collide(Area area) {
		return parallelogram.intersects(area.rectangle);
	}

	public void recalculate() {

		parallelogram = new GeneralPath.Double(GeneralPath.WIND_EVEN_ODD, 4);
		parallelogram.moveTo(p1.getX(), p1.getY());
		parallelogram.lineTo(p2.getX(), p2.getY());
		parallelogram.lineTo(p3.getX(), p3.getY());
		parallelogram.lineTo(p4.getX(), p4.getY());
		parallelogram.closePath();
	}

	@Override
	public String toString() {
		return "Area {" + parallelogram.toString() + "}";
	}
}
