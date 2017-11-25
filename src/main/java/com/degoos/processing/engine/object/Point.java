package com.degoos.processing.engine.object;

import com.degoos.processing.engine.Processing;
import com.degoos.processing.engine.util.CoordinatesUtils;
import com.degoos.processing.engine.util.Validate;
import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2f;
import java.awt.Color;

public class Point extends GObject {

	private Vector2d position;
	private float size;
	private Color color;

	public Point(Vector2d position) {
		this(false, 0, 0, position, null);
	}

	public Point(Vector2d position, float size) {
		this(false, 0, 0, position, null, size);
	}

	public Point(Vector2d position, Color color) {
		this(false, 0, 0, position, color);
	}

	public Point(Vector2d position, Color color, float size) {
		this(false, 0, 0, position, color, size);
	}

	public Point(boolean visible, int drawPriority, int tickPriority, Vector2d position, Color color) {
		this(visible, drawPriority, tickPriority, position, color, 1);
	}

	public Point(boolean visible, int drawPriority, int tickPriority, Vector2d position, Color color, float size) {
		super(visible, drawPriority, tickPriority);
		setPosition(position);
		setColor(color);
		this.size = size;
	}

	public Vector2d getPosition() {
		return position;
	}

	public Point setPosition(Vector2d position) {
		Validate.notNull(position, "Position vector cannot be null!");
		this.position = position;
		return this;
	}

	public Color getColor() {
		return color;
	}

	public Point setColor(Color color) {
		this.color = color;
		return this;
	}

	public float getSize() {
		return size;
	}

	public Point setSize(float size) {
		this.size = size;
		return this;
	}

	@Override
	public void draw(Processing core) {
		if (color == null) core.noStroke();
		else core.stroke(color.getRGB());
		core.strokeWeight(size);
		Vector2f pPosition = CoordinatesUtils.transformIntoProcessingCoordinates(position);
		core.point(pPosition.getX(), pPosition.getY(), getDrawPriority() / 1000F);
	}

	@Override
	public void onTick(long dif) {

	}
}
