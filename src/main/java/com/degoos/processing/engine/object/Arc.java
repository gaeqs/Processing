package com.degoos.processing.engine.object;

import com.degoos.processing.engine.Processing;
import com.degoos.processing.engine.enums.EnumArcMode;
import com.degoos.processing.engine.util.CoordinatesUtils;
import com.degoos.processing.engine.util.Validate;
import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2f;
import java.awt.Color;

public class Arc extends GObject {

	private Vector2d max, min;
	private Color fillColor, lineColor;
	private float lineTransparency, fillTransparency;
	private float startAngle, stopAngle, lineSize;
	private EnumArcMode arcMode;

	public Arc(Vector2d max, Vector2d min, float startAngle, float stopAngle) {
		this(false, 0, 0, max, min, null, null, startAngle, stopAngle, 1, EnumArcMode.PIE);
	}

	public Arc(Vector2d max, Vector2d min, float startAngle, float stopAngle, EnumArcMode arcMode) {
		this(false, 0, 0, max, min, null, null, startAngle, stopAngle, 1, arcMode);
	}

	public Arc(boolean visible, double drawPriority, double tickPriority, Vector2d max, Vector2d min, float startAngle, float stopAngle) {
		this(visible, drawPriority, tickPriority, max, min, null, null, startAngle, stopAngle, 1, EnumArcMode.PIE);
	}

	public Arc(boolean visible, double drawPriority, double tickPriority, Vector2d max, Vector2d min, Color fillColor, Color lineColor, float startAngle, float
		stopAngle,
		float lineSize, EnumArcMode arcMode) {
		super(visible, drawPriority, tickPriority);
		Validate.notNull(max, "Max cannot be null!");
		Validate.notNull(min, "Min cannot be null!");
		Validate.notNull(arcMode, "Arc mode cannot be null!");
		this.max = max;
		this.min = min;
		this.fillColor = fillColor;
		this.lineColor = lineColor;
		this.startAngle = startAngle;
		this.stopAngle = stopAngle;
		this.lineSize = lineSize;
		this.arcMode = arcMode;
		this.lineTransparency = fillTransparency = 1;
		finishLoad();
	}

	public Vector2d getMax() {
		return max;
	}

	public void setMax(Vector2d max) {
		Validate.notNull(max, "Max cannot be null!");
		this.max = max;
	}

	public Vector2d getMin() {
		return min;
	}

	public void setMin(Vector2d min) {
		Validate.notNull(min, "Min cannot be null!");
		this.min = min;
	}

	public Color getLineColor() {
		return lineColor;
	}

	public Arc setLineColor(Color lineColor) {
		this.lineColor = lineColor;
		return this;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public Arc setFillColor(Color fillColor) {
		this.fillColor = fillColor;
		return this;
	}

	public Arc setFullColor(Color color) {
		this.fillColor = color;
		this.lineColor = color;
		return this;
	}

	public float getStartAngle() {
		return startAngle;
	}

	public Arc setStartAngle(float startAngle) {
		this.startAngle = startAngle;
		return this;
	}

	public float getStopAngle() {
		return stopAngle;
	}

	public Arc setStopAngle(float stopAngle) {
		this.stopAngle = stopAngle;
		return this;
	}

	public float getLineSize() {
		return lineSize;
	}

	public Arc setLineSize(float lineSize) {
		this.lineSize = lineSize;
		return this;
	}

	public EnumArcMode getArcMode() {
		return arcMode;
	}

	public void setArcMode(EnumArcMode arcMode) {
		Validate.notNull(arcMode, "Arc mode cannot be null!");
		this.arcMode = arcMode;
	}

	public float getLineTransparency() {
		return lineTransparency;
	}

	public Arc setLineTransparency(float lineTransparency) {
		this.lineTransparency = lineTransparency;
		return this;
	}

	public float getFillTransparency() {
		return fillTransparency;
	}

	public Arc setFillTransparency(float fillTransparency) {
		this.fillTransparency = fillTransparency;
		return this;
	}

	public Arc setFullTransparency(float transparency) {
		this.fillTransparency = lineTransparency = transparency;
		return this;
	}

	@Override
	public void draw(Processing core) {
		core.strokeWeight(lineSize);
		if (lineColor == null) core.noStroke();
		else core.stroke(lineColor.getRGB(), fillTransparency * 255);
		if (fillColor == null) core.noFill();
		else core.fill(fillColor.getRGB(), fillTransparency * 255);
		Vector2f pMin = CoordinatesUtils.toProcessingCoordinates(min);
		Vector2f pMax = CoordinatesUtils.toProcessingCoordinates(max);
		core.arc(pMin.getX(), pMin.getY(), pMax.getX(), pMax.getY(), startAngle, stopAngle, arcMode.getId());
	}

	@Override
	public void onTick(long dif) {

	}

	@Override
	public boolean isLoaded() {
		return false;
	}
}
