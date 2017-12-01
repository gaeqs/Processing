package com.degoos.processing.engine.object;

import com.degoos.processing.engine.Processing;
import com.degoos.processing.engine.util.CoordinatesUtils;
import com.degoos.processing.engine.util.Validate;
import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2f;
import java.awt.Color;

public class Text extends GObject {

	private String text;
	private Vector2d position;
	private Color color;
	private float size;
	private Font font;

	public Text(String text, Vector2d position) {
		this(false, 0, 0, text, position);
	}

	public Text(boolean visible, double drawPriority, double tickPriority, String text, Vector2d position) {
		this(visible, drawPriority, tickPriority, text, position, null, 10, null);
	}

	public Text(boolean visible, double drawPriority, double tickPriority, String text, Vector2d position, Color color) {
		this(visible, drawPriority, tickPriority, text, position, color, 10, null);
	}

	public Text(boolean visible, double drawPriority, double tickPriority, String text, Vector2d position, float size) {
		this(visible, drawPriority, tickPriority, text, position, null, size, null);
	}

	public Text(boolean visible, double drawPriority, double tickPriority, String text, Vector2d position, Font font) {
		this(visible, drawPriority, tickPriority, text, position, null, 10, font);
	}

	public Text(boolean visible, double drawPriority, double tickPriority, String text, Vector2d position, Color color, Font font) {
		this(visible, drawPriority, tickPriority, text, position, color, 10, font);
	}

	public Text(boolean visible, double drawPriority, double tickPriority, String text, Vector2d start, float size, Font font) {
		this(visible, drawPriority, tickPriority, text, start, null, size, font);
	}

	public Text(boolean visible, double drawPriority, double tickPriority, String text, Vector2d position, Color color, float size) {
		this(visible, drawPriority, tickPriority, text, position, color, size, null);
	}

	public Text(boolean visible, double drawPriority, double tickPriority, String text, Vector2d position, Color color, float size, Font font) {
		super(visible, drawPriority, tickPriority);
		this.text = text;
		this.position = position;
		this.color = color;
		this.size = size;
		this.font = font;
	}


	public String getText() {
		return text;
	}

	public void setText(String text) {
		Validate.notNull(text, "Text cannot be null!");
		this.text = text;
	}

	public Vector2d getPosition() {
		return position;
	}

	public void setPosition(Vector2d position) {
		Validate.notNull(position, "Start cannot be null!");
		this.position = position;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	@Override
	public void draw(Processing core) {
		if (color == null) core.noFill();
		else core.fill(color.getRGB());
		Vector2f pStart = CoordinatesUtils.toProcessingCoordinates(position);
		if (font != null) core.textFont(font.getHandled());
		core.textSize(size);
		core.text(text, pStart.getX(), pStart.getY(), (float) getDrawPriority() / 1000F);
	}

	@Override
	public void onTick(long dif) {

	}
}
