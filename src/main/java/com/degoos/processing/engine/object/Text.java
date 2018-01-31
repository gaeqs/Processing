package com.degoos.processing.engine.object;

import com.degoos.processing.engine.Processing;
import com.degoos.processing.engine.enums.EnumTextAlign;
import com.degoos.processing.engine.enums.EnumTextHeight;
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
	private EnumTextAlign textAlign;
	private EnumTextHeight textHeight;
	private float opacity;

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
		this.opacity = 1;
		setTextAlign(null);
		setTextHeight(null);
		finishLoad();
	}

	public Text(boolean visible, double drawPriority, double tickPriority, String text, Vector2d position, Color color, float size, Font font, EnumTextAlign textAlign,
		EnumTextHeight textHeight) {
		super(visible, drawPriority, tickPriority);
		this.text = text;
		this.position = position;
		this.color = color;
		this.size = size;
		this.font = font;
		this.opacity = 1;
		setTextAlign(textAlign);
		setTextHeight(textHeight);
		finishLoad();
	}


	public String getText() {
		return text;
	}

	public Text setText(String text) {
		Validate.notNull(text, "Text cannot be null!");
		this.text = text;
		return this;
	}

	public Vector2d getPosition() {
		return position;
	}

	public Text setPosition(Vector2d position) {
		Validate.notNull(position, "Start cannot be null!");
		this.position = position;
		return this;
	}

	public Color getColor() {
		return color;
	}

	public Text setColor(Color color) {
		this.color = color;
		return this;
	}

	public Font getFont() {
		return font;
	}

	public Text setFont(Font font) {
		this.font = font;
		return this;
	}

	public float getSize() {
		return size;
	}

	public Text setSize(float size) {
		this.size = size;
		return this;
	}

	public EnumTextAlign getTextAlign() {
		return textAlign;
	}

	public Text setTextAlign(EnumTextAlign textAlign) {
		this.textAlign = textAlign == null ? EnumTextAlign.LEFT : textAlign;
		return this;
	}

	public EnumTextHeight getTextHeight() {
		return textHeight;
	}

	public Text setTextHeight(EnumTextHeight textHeight) {
		this.textHeight = textHeight == null ? EnumTextHeight.BOTTOM : textHeight;
		return this;
	}

	public float getOpacity() {
		return opacity;
	}

	public Text setOpacity(float opacity) {
		this.opacity = opacity;
		return this;
	}

	@Override
	public void draw(Processing core) {
		core.pushStyle();
		if (color == null) core.noFill();
		else core.fill(color.getRGB(), opacity * 255);
		Vector2f pStart = CoordinatesUtils.toProcessingCoordinates(position);
		if (font != null) core.textFont(font.getHandled());
		core.textSize(size);
		core.textAlign(textAlign.getId(), textHeight.getId());
		core.text(text, pStart.getX(), pStart.getY());
		core.popStyle();
	}

	@Override
	public void onTick(long dif) {

	}
}
