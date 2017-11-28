package com.degoos.processing.engine;

import com.degoos.processing.engine.enums.EnumKeyboardKey;
import com.degoos.processing.engine.enums.EnumMouseKey;
import com.degoos.processing.engine.event.draw.AfterDrawEvent;
import com.degoos.processing.engine.event.draw.BeforeDrawEvent;
import com.degoos.processing.engine.event.keyboard.KeyPressEvent;
import com.degoos.processing.engine.event.keyboard.KeyReleaseEvent;
import com.degoos.processing.engine.event.keyboard.KeyTypeEvent;
import com.degoos.processing.engine.event.mouse.MouseClickEvent;
import com.degoos.processing.engine.event.mouse.MouseDragEvent;
import com.degoos.processing.engine.event.mouse.MouseMoveEvent;
import com.degoos.processing.engine.event.mouse.MousePressEvent;
import com.degoos.processing.engine.event.mouse.MouseReleaseEvent;
import com.degoos.processing.engine.event.screen.ScreenResizeEvent;
import com.degoos.processing.engine.object.GObject;
import com.degoos.processing.engine.object.inheritance.Parent;
import com.degoos.processing.engine.util.Validate;
import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2i;
import java.awt.Color;
import java.util.Comparator;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class Processing extends PApplet {

	private static Processing instance;
	private static Vector2i size;
	private Color background;
	private long lastTick;

	public static void startEngine(Vector2i size) {
		Validate.notNull(size, "Size cannot be null!");
		Processing.size = size;
		PApplet.main("com.degoos.processing.engine.Processing");
	}

	public Processing() {
		instance = this;
		lastTick = System.currentTimeMillis();
		background = Color.BLACK;
	}

	public static Processing getInstance() {
		return instance;
	}

	public Color getBackground() {
		return background;
	}

	public void setBackground(Color background) {
		this.background = background;
	}

	public void rezise(int x, int y) {
		resize(new Vector2i(x, y));
	}

	public void resize(Vector2i vector) {
		size(vector.getX(), vector.getY());
	}

	@Override
	public void settings() {
		size(size.getX(), size.getY(), P2D);
	}

	@Override
	public void setup() {
		surface.setResizable(true);
		ellipseMode(CORNERS);
	}

	@Override
	public void draw() {
		System.gc();

		Vector2i currentSize = new Vector2i(width, height);
		if (!size.equals(currentSize)) {
			ScreenResizeEvent event = new ScreenResizeEvent(size, currentSize);
			Engine.getEventManager().callEvent(event);
			size = event.getNewSize();
			if (!currentSize.equals(event.getNewSize())) resize(event.getNewSize());
		}

		long dif = System.currentTimeMillis() - lastTick;
		lastTick = System.currentTimeMillis();
		Engine.getEventManager().callEvent(new BeforeDrawEvent(dif));
		background(background.getRGB());
		Engine.getObjectManager().getObjects().stream().sorted(Comparator.comparingInt(GObject::getTickPriority)).forEach(object -> {
			object.onTick(dif);
			if (object instanceof Parent) onChildrenTick((Parent) object, dif);
		});
		Engine.getObjectManager().getObjects().stream().sorted(Comparator.comparingInt(GObject::getDrawPriority)).forEach(object -> {
			if (object.isVisible()) object.draw(this);
		});

		Engine.getEventManager().callEvent(new AfterDrawEvent(dif));
	}

	private void onChildrenTick(Parent parent, long dif) {
		parent.getChildren().stream().sorted(Comparator.comparingInt(GObject::getDrawPriority)).forEach(child -> {
			child.onTick(dif);
			if (child instanceof Parent) onChildrenTick((Parent) child, dif);
		});
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		MouseClickEvent mouseEvent = new MouseClickEvent(new Vector2d(
			(double) event.getX() / (double) width, (double) (height - event.getY()) / (double) height), EnumMouseKey.getValueById(event.getButton())
			.orElse(EnumMouseKey.UNKNOWN), event.getCount());
		Engine.getEventManager().callEvent(mouseEvent);
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		MouseDragEvent mouseEvent = new MouseDragEvent(new Vector2d(
			(double) event.getX() / (double) width, (double) (height - event.getY()) / (double) height), EnumMouseKey.getValueById(event.getButton())
			.orElse(EnumMouseKey.UNKNOWN), event.getCount());
		Engine.getEventManager().callEvent(mouseEvent);
	}

	@Override
	public void mousePressed(MouseEvent event) {
		MousePressEvent mouseEvent = new MousePressEvent(new Vector2d(
			(double) event.getX() / (double) width, (double) (height - event.getY()) / (double) height), EnumMouseKey.getValueById(event.getButton())
			.orElse(EnumMouseKey.UNKNOWN), event.getCount());
		Engine.getEventManager().callEvent(mouseEvent);
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		MouseReleaseEvent mouseEvent = new MouseReleaseEvent(new Vector2d(
			(double) event.getX() / (double) width, (double) (height - event.getY()) / (double) height), EnumMouseKey.getValueById(event.getButton())
			.orElse(EnumMouseKey.UNKNOWN), event.getCount());
		Engine.getEventManager().callEvent(mouseEvent);
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		MouseMoveEvent mouseEvent = new MouseMoveEvent(new Vector2d(
			(double) event.getX() / (double) width, (double) (height - event.getY()) / (double) height), EnumMouseKey.getValueById(event.getButton())
			.orElse(EnumMouseKey.UNKNOWN), event.getCount());
		Engine.getEventManager().callEvent(mouseEvent);
	}

	@Override
	public void keyPressed(KeyEvent event) {
		KeyPressEvent keyEvent = new KeyPressEvent(EnumKeyboardKey.getById(event.getKeyCode()).orElse(EnumKeyboardKey.UNDEFINED), event.getKey());
		Engine.getEventManager().callEvent(keyEvent);
	}

	@Override
	public void keyReleased(KeyEvent event) {
		KeyReleaseEvent keyEvent = new KeyReleaseEvent(EnumKeyboardKey.getById(event.getKeyCode()).orElse(EnumKeyboardKey.UNDEFINED), event.getKey());
		Engine.getEventManager().callEvent(keyEvent);
	}

	@Override
	public void keyTyped(KeyEvent event) {
		KeyTypeEvent keyEvent = new KeyTypeEvent(event.getKey());
		Engine.getEventManager().callEvent(keyEvent);
	}
}
