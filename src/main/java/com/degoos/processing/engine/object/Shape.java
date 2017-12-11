package com.degoos.processing.engine.object;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.Processing;
import com.degoos.processing.engine.object.inheritance.Parent;
import com.degoos.processing.engine.util.CoordinatesUtils;
import com.degoos.processing.engine.util.Validate;
import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2f;
import com.flowpowered.math.vector.Vector2i;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import processing.core.PConstants;
import processing.core.PShape;

public class Shape extends GObject implements Parent {

	private Vector2d origin;
	private List<Vector2d> vertexes;
	private Map<Vector2d, Vector2i> uvMaps;
	private Color lineColor, fillColor;
	private Image texture;
	private List<ShapeChild> children;
	private float transparency;
	private float rotation;

	public Shape(Vector2d origin) {
		this(origin, new ArrayList<>());
	}

	public Shape(Vector2d origin, List<Vector2d> vertexes) {
		this(false, 0, 0, origin, vertexes);
	}

	public Shape(boolean visible, double drawPriority, double tickPriority, Vector2d origin) {
		this(visible, drawPriority, tickPriority, origin, null);
	}

	public Shape(boolean visible, double drawPriority, double tickPriority, Vector2d origin, List<Vector2d> vertexes) {
		super(visible, drawPriority, tickPriority);
		setOrigin(origin);
		setVertexes(vertexes);
		setUvMaps(null);
		setChildren(null);
		setFullColor(null);
		this.transparency = 255;
	}

	public Vector2d getOrigin() {
		return origin;
	}

	public Shape setOrigin(Vector2d origin) {
		Validate.notNull(origin, "Origin cannot be null!");
		this.origin = origin;
		return this;
	}

	public List<Vector2d> getVertexes() {
		return vertexes;
	}

	public Shape setVertexes(List<Vector2d> vertexes) {
		this.vertexes = vertexes == null ? new ArrayList<>() : new ArrayList<>(vertexes);
		return this;
	}

	public Shape addVertex(Vector2d vertex) {
		vertexes.add(vertex);
		return this;
	}

	public Map<Vector2d, Vector2i> getUvMaps() {
		return uvMaps;
	}

	public Shape setUvMaps(Map<Vector2d, Vector2i> uvMaps) {
		this.uvMaps = uvMaps == null ? new HashMap<>() : uvMaps;
		return this;
	}

	public Shape addUvMap(Vector2d vertex, Vector2i uv) {
		uvMaps.put(vertex, uv);
		return this;
	}

	public Shape addVertexWithUv(Vector2d vertex, Vector2i uv) {
		vertexes.add(vertex);
		uvMaps.put(vertex, uv);
		return this;
	}

	@Override
	public List<ShapeChild> getChildren() {
		return children;
	}

	public void setChildren(List<ShapeChild> children) {
		this.children = children == null ? new ArrayList<>() : children;
	}

	public void addChild(ShapeChild child) {
		children.add(child);
	}

	public Color getLineColor() {
		return lineColor;
	}

	public Shape setLineColor(Color lineColor) {
		this.lineColor = lineColor;
		refreshShape();
		return this;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public Shape setFillColor(Color fillColor) {
		this.fillColor = fillColor;
		return this;
	}

	public Shape setFullColor(Color color) {
		this.fillColor = color;
		this.lineColor = color;
		return this;
	}

	public Image getTexture() {
		return texture;
	}

	public Shape setTexture(Image texture) {
		this.texture = texture;
		return this;
	}

	public float getTransparency() {
		return transparency;
	}

	public Shape setTransparency(float transparency) {
		this.transparency = transparency;
		return this;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	@Override
	public void draw(Processing core) {
		Vector2f pOrigin = CoordinatesUtils.toProcessingCoordinates(origin);
		core.tint(255, transparency);
		core.shape(refreshShape(), pOrigin.getX(), pOrigin.getY());
		core.tint(255, 255);
	}

	@Override
	public void onTick(long dif) {

	}

	protected PShape refreshShape() {
		try {
			Processing core = Engine.getCore();
			PShape parent = children.isEmpty() ? null : core.createShape(Processing.GROUP);
			PShape handled = core.createShape();
			handled.beginShape();
			handled.noFill();
			handled.noStroke();
			if (fillColor == null) handled.noFill();
			else handled.fill(fillColor.getRGB());
			if (lineColor == null) handled.noStroke();
			else handled.stroke(lineColor.getRGB());
			if (texture != null) {
				handled.textureMode(PConstants.NORMAL);
				handled.texture(texture.getHandled());
			} else handled.noTexture();
			vertexes.forEach(vector2d -> {
				Vector2f pVector = CoordinatesUtils.toProcessingCoordinates(vector2d, false);
				Vector2i uv = uvMaps.get(vector2d);
				if (uv != null) handled.vertex(pVector.getX(), pVector.getY(), uv.getX(), uv.getY() == 0 ? 1 : 0);
				else handled.vertex(pVector.getX(), pVector.getY());
			});
			children.stream().filter(GObject::isVisible).sorted(Comparator.comparingDouble(GObject::getDrawPriority)).forEach(child -> {
				if (parent != null) parent.addChild(child.refreshShape());
			});
			handled.endShape(PConstants.CLOSE);
			handled.rotate(rotation);
			if (parent != null) {
				parent.addChild(handled);
				return parent;
			}
			return handled;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
