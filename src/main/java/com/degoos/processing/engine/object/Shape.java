package com.degoos.processing.engine.object;

import com.degoos.processing.engine.Processing;
import com.degoos.processing.engine.object.inheritance.Parent;
import com.degoos.processing.engine.util.CoordinatesUtils;
import com.degoos.processing.engine.util.Validate;
import com.degoos.processing.game.entity.Player;
import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2f;
import com.flowpowered.math.vector.Vector2i;
import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import processing.core.PConstants;
import processing.core.PShape;

public class Shape extends GObject implements Parent {

	private Vector2d origin;
	private Map<Vector2d, Vector2i> uvMaps;
	private Color lineColor, fillColor;
	private Image texture;
	private List<ShapeChild> children;
	private float imageOpacity, fillOpacity, lineOpacity;
	private float rotation;
	private PShape shape;

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
		uvMaps = new HashMap<>();
		shape = Processing.getInstance().createShape();
		shape.beginShape();
		shape.textureMode(PConstants.NORMAL);
		if (vertexes != null) vertexes.forEach(this::addVertex);
		setOrigin(origin);
		setChildren(null);
		setFullColor(null);
		this.imageOpacity = fillOpacity = lineOpacity = 1;
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

	public Shape addVertex(Vector2d vertex) {
		Vector2i uv = uvMaps.get(vertex);
		Vector2f pVector = CoordinatesUtils.toProcessingCoordinates(vertex, false);
		if (uv != null) shape.vertex(pVector.getX(), pVector.getY(), uv.getX(), uv.getY() == 0 ? 1 : 0);
		else shape.vertex(pVector.getX(), pVector.getY());
		return this;
	}

	public Map<Vector2d, Vector2i> getUvMaps() {
		return uvMaps;
	}

	public Shape addVertexWithUv(Vector2d vertex, Vector2i uv) {
		uvMaps.put(vertex, uv);
		Vector2f pVector = CoordinatesUtils.toProcessingCoordinates(vertex, false);
		if (uv != null) shape.vertex(pVector.getX(), pVector.getY(), uv.getX(), uv.getY() == 0 ? 1 : 0);
		else shape.vertex(pVector.getX(), pVector.getY());
		return this;
	}

	@Override
	public List<ShapeChild> getChildren() {
		return children;
	}

	public void setChildren(List<ShapeChild> children) {
		this.children = children == null ? new CopyOnWriteArrayList<>() : new CopyOnWriteArrayList<>(children);
		for (int i = 0; i < shape.getVertexCount(); i++)
			shape.removeChild(i);
		this.children.stream().filter(GObject::isVisible).sorted(Comparator.comparingDouble(GObject::getDrawPriority)).forEach(child -> {
			if (shape != null) shape.addChild(child.getShape());
		});
	}

	public void addChild(ShapeChild child) {
		children.add(child);
		shape.addChild(child.getShape());
	}

	public Color getLineColor() {
		return lineColor;
	}

	public Shape setLineColor(Color lineColor) {
		this.lineColor = lineColor;
		if (lineColor == null) shape.noStroke();
		else shape.stroke(lineColor.getRGB(), lineOpacity * 255);
		return this;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public Shape setFillColor(Color fillColor) {
		this.fillColor = fillColor;
		if (fillColor == null) shape.noFill();
		else shape.fill(fillColor.getRGB(), fillOpacity * 255);
		return this;
	}

	public Shape setFullColor(Color color) {
		this.fillColor = color;
		this.lineColor = color;
		if (fillColor == null) shape.noFill();
		else shape.fill(fillColor.getRGB(), fillOpacity * 255);
		if (lineColor == null) shape.noStroke();
		else shape.stroke(lineColor.getRGB(), lineOpacity * 255);
		return this;
	}

	public Image getTexture() {
		return texture;
	}

	public Shape setTexture(Image texture) {
		this.texture = texture;
		boolean b = isOpenShape();
		if (!isOpenShape()) shape.beginShape();
		if (texture != null) {
			shape.texture(texture.getHandled());
		} else shape.noTexture();
		shape.endShape(PConstants.CLOSE);
		return this;
	}

	public float getImageOpacity() {
		return imageOpacity;
	}

	public Shape setImageOpacity(float imageOpacity) {
		this.imageOpacity = imageOpacity;
		return this;
	}

	public float getFillOpacity() {
		return fillOpacity;
	}

	public Shape setFillOpacity(float fillOpacity) {
		this.fillOpacity = fillOpacity;
		if (fillColor == null) shape.noFill();
		else shape.fill(fillColor.getRGB(), fillOpacity * 255);
		return this;
	}

	public float getLineOpacity() {
		return lineOpacity;
	}

	public Shape setLineOpacity(float lineOpacity) {
		this.lineOpacity = lineOpacity;
		if (lineColor == null) shape.noStroke();
		else shape.stroke(lineColor.getRGB(), lineOpacity * 255);
		return this;
	}

	public Shape setFullOpacity(float opacity) {
		this.lineOpacity = fillOpacity = imageOpacity = opacity;
		if (fillColor == null) shape.noFill();
		else shape.fill(fillColor.getRGB(), fillOpacity * 255);
		if (lineColor == null) shape.noStroke();
		else shape.stroke(lineColor.getRGB(), lineOpacity * 255);
		return this;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		shape.rotate(-this.rotation);
		this.rotation = rotation;
		shape.rotate(rotation);
	}

	public boolean isOpenShape() {
		try {
			Field field = PShape.class.getField("openShape");
			field.setAccessible(true);
			return field.getBoolean(shape);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public PShape getShape() {
		return shape;
	}

	@Override
	public void draw(Processing core) {
		if (shape.getVertexCount() == 0 || (fillColor == null && lineColor == null && texture == null)) return;
		Vector2f pOrigin = CoordinatesUtils.toProcessingCoordinates(origin);
		core.tint(255, imageOpacity * 255);
		if (texture != null) {
			shape.beginShape();
			shape.texture(texture.getHandled());
		} else if (this instanceof Player) System.out.println(this);
		if (!shape.isClosed()) shape.endShape(Processing.CLOSE);
		core.shape(shape, pOrigin.getX(), pOrigin.getY());
		core.tint(255, 255);
	}

	@Override
	public void onTick(long dif) {

	}
}
