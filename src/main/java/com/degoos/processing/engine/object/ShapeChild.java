package com.degoos.processing.engine.object;

import com.degoos.processing.engine.Processing;
import com.flowpowered.math.vector.Vector2d;
import java.util.List;

public class ShapeChild extends Shape {


	public ShapeChild(Vector2d origin) {
		super(origin);
	}

	public ShapeChild(Vector2d origin, List<Vector2d> vertexes) {
		super(origin, vertexes);
	}

	public ShapeChild(boolean visible, int drawPriority, int tickPriority, Vector2d origin) {
		super(visible, drawPriority, tickPriority, origin);
	}

	public ShapeChild(boolean visible, int drawPriority, int tickPriority, Vector2d origin, List<Vector2d> vertexes) {
		super(visible, drawPriority, tickPriority, origin, vertexes);
	}

	@Override
	public void draw(Processing core) {
	}
}
