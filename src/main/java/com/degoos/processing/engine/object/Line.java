package com.degoos.processing.engine.object;

import com.degoos.processing.engine.Processing;
import com.degoos.processing.engine.util.CoordinatesUtils;
import com.degoos.processing.engine.util.Validate;
import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2f;
import java.awt.Color;

public class Line extends GObject {

	private Vector2d start, end;
	private float size;
	private Color color;
	private float transparency;

	public Line(Vector2d start, Vector2d end) {
		this(false, 0, 0, start, end, null);
	}

	public Line(Vector2d start, Vector2d end, float size) {
		this(false, 0, 0, start, end, null, size);
	}

	public Line(Vector2d start, Vector2d end, Color color) {
		this(false, 0, 0, start, end, color);
	}

	public Line(Vector2d start, Vector2d end, Color color, float size) {
		this(false, 0, 0, start, end, color, size);
	}

	public Line(boolean visible, double drawPriority, double tickPriority, Vector2d start, Vector2d end, Color color) {
		this(visible, drawPriority, tickPriority, start, end, color, 1);
	}

	public Line(boolean visible, double drawPriority, double tickPriority, Vector2d start, Vector2d end, Color color, float size) {
		super(visible, drawPriority, tickPriority);
		setStart(start);
		setEnd(end);
		setColor(color);
		this.size = size;
		this.transparency = 1;
		finishLoad();
	}

	public Vector2d getStart() {
		return start;
	}

	public Line setStart(Vector2d start) {
		Validate.notNull(start, "Start vector cannot be null!");
		this.start = start;
		return this;
	}

	public Vector2d getEnd() {
		return end;
	}

	public Line setEnd(Vector2d end) {
		Validate.notNull(end, "End vector cannot be null!");
		this.end = end;
		return this;
	}

	public Color getColor() {
		return color;
	}

	public Line setColor(Color color) {
		this.color = color;
		return this;
	}

	public float getSize() {
		return size;
	}

	public Line setSize(float size) {
		this.size = size;
		return this;
	}

	public float getTransparency() {
		return transparency;
	}

	public Line setTransparency(float transparency) {
		this.transparency = transparency;
		return this;
	}

	@Override
	public void draw(Processing core) {
		if (color == null) core.noStroke();
		else core.stroke(color.getRGB(), transparency * 255);
		core.strokeWeight(size);
		Vector2f pStart = CoordinatesUtils.toProcessingCoordinates(start);
		Vector2f pEnd = CoordinatesUtils.toProcessingCoordinates(end);
		core.line(pStart.getX(), pStart.getY(), pEnd.getX(), pEnd.getY());
	}

	@Override
	public void onTick(long dif) {

	}
}
