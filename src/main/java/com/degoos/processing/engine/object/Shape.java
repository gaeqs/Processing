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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import processing.core.PConstants;
import processing.core.PShape;

public class Shape extends GObject implements Parent {

	private Vector2d origin;
	private List<Vector2d> vertexes;
	private Map<Vector2d, Vector2i> uvMaps;
	private Color lineColor, fillColor;
	private Image texture;
	private List<ShapeChild> children;
	private float imageOpacity, fillOpacity, lineOpacity;
	private float rotation;
	private PShape lastShape;
	private boolean hasChanged;

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
		this.imageOpacity = fillOpacity = lineOpacity = 1;
		hasChanged = true;
		finishLoad();
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
		hasChanged = true;
		return vertexes;
	}

	public Shape setVertexes(List<Vector2d> vertexes) {
		this.vertexes = vertexes == null ? new CopyOnWriteArrayList<>() : new CopyOnWriteArrayList<>(vertexes);
		hasChanged = true;
		return this;
	}

	public Shape addVertex(Vector2d vertex) {
		vertexes.add(vertex);
		hasChanged = true;
		return this;
	}

	public Map<Vector2d, Vector2i> getUvMaps() {
		hasChanged = true;
		return uvMaps;
	}

	public Shape setUvMaps(Map<Vector2d, Vector2i> uvMaps) {
		this.uvMaps = uvMaps == null ? new ConcurrentHashMap<>() : new ConcurrentHashMap<>(uvMaps);
		hasChanged = true;
		return this;
	}

	public Shape addUvMap(Vector2d vertex, Vector2i uv) {
		uvMaps.put(vertex, uv);
		hasChanged = true;
		return this;
	}

	public Shape addVertexWithUv(Vector2d vertex, Vector2i uv) {
		vertexes.add(vertex);
		uvMaps.put(vertex, uv);
		hasChanged = true;
		return this;
	}

	@Override
	public List<ShapeChild> getChildren() {
		hasChanged = true;
		return children;
	}

	public void setChildren(List<ShapeChild> children) {
		this.children = children == null ? new CopyOnWriteArrayList<>() : new CopyOnWriteArrayList<>(children);
		hasChanged = true;
	}

	public void addChild(ShapeChild child) {
		children.add(child);
		hasChanged = true;
	}

	public Color getLineColor() {
		return lineColor;
	}

	public Shape setLineColor(Color lineColor) {
		this.lineColor = lineColor;
		hasChanged = true;
		return this;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public Shape setFillColor(Color fillColor) {
		this.fillColor = fillColor;
		hasChanged = true;
		return this;
	}

	public Shape setFullColor(Color color) {
		this.fillColor = color;
		this.lineColor = color;
		hasChanged = true;
		return this;
	}

	public Image getTexture() {
		return texture;
	}

	public Shape setTexture(Image texture) {
		this.texture = texture;
		hasChanged = true;
		return this;
	}

	public float getImageOpacity() {
		return imageOpacity;
	}

	public Shape setImageOpacity(float imageOpacity) {
		this.imageOpacity = imageOpacity;
		hasChanged = true;
		return this;
	}

	public float getFillOpacity() {
		return fillOpacity;
	}

	public Shape setFillOpacity(float fillOpacity) {
		this.fillOpacity = fillOpacity;
		hasChanged = true;
		return this;
	}

	public float getLineOpacity() {
		return lineOpacity;
	}

	public Shape setLineOpacity(float lineOpacity) {
		this.lineOpacity = lineOpacity;
		hasChanged = true;
		return this;
	}

	public Shape setFullOpacity(float opacity) {
		this.lineOpacity = fillOpacity = imageOpacity = opacity;
		hasChanged = true;
		return this;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
		hasChanged = true;
	}

	@Override
	public void draw(Processing core) {
		core.pushStyle();
		if (vertexes.isEmpty() || (fillColor == null && lineColor == null && texture == null)) return;
		Vector2f pOrigin = CoordinatesUtils.toProcessingCoordinates(origin);
		core.tint(255, imageOpacity * 255);
		if (hasChanged || texture instanceof Animation) {
			lastShape = refreshShape();
			hasChanged = false;
		}
		core.shape(lastShape, pOrigin.getX(), pOrigin.getY());
		core.noTint();
		core.popStyle();
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
			else handled.fill(fillColor.getRGB(), fillOpacity * 255);
			if (lineColor == null) handled.noStroke();
			else handled.stroke(lineColor.getRGB(), lineOpacity * 255);
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
