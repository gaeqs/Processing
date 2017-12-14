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
import java.util.Iterator;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class Processing extends PApplet {

	private static Processing instance;
	private static Vector2i size;
	private Color background;
	private long lastTick;
	private boolean active;

	public static void startEngine(Vector2i size) {
		Validate.notNull(size, "Size cannot be null!");
		Processing.size = size;
		PApplet.main("com.degoos.processing.engine.Processing");
	}

	public Processing() {
		instance = this;
		lastTick = System.currentTimeMillis();
		background = Color.BLACK;
		active = true;
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
		frameRate(500);
		surface.setResizable(true);
		ellipseMode(CORNERS);
		textMode(MODEL);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> active = false));
	}

	@Override
	public void draw() {
		try {
			clear();
			Vector2i currentSize = new Vector2i(width, height);

			long dif = System.currentTimeMillis() - lastTick;
			lastTick = System.currentTimeMillis();

			if (!size.equals(currentSize)) {
				ScreenResizeEvent event = new ScreenResizeEvent(size, currentSize);
				Engine.getEventManager().callEvent(event);
				size = event.getNewSize();
				if (!currentSize.equals(event.getNewSize())) resize(event.getNewSize());
			}
			Engine.getEventManager().callEvent(new BeforeDrawEvent(dif));
			background(background.getRGB());

			Iterator<GObject> iterator = Engine.getObjectManager().getObjects().stream().sorted(Comparator.comparingDouble(GObject::getTickPriority)).iterator();
			while (iterator.hasNext()) {
				GObject object = iterator.next();
				if (!object.isLoaded()) continue;
				try {

					long now = System.currentTimeMillis();
					object.onTick(dif);
					if (object instanceof Parent) onChildrenTick((Parent) object, dif);
					now = System.currentTimeMillis() - now;
					if (now > 10) System.out.println("[WARN TICK] " + object + " took " + now + " millis!");
				} catch (Exception ex) {
					System.err.println("Exception " + ex + " for object " + object);
					ex.printStackTrace();
				}
			}

			iterator = Engine.getObjectManager().getObjects().stream().sorted(Comparator.comparingDouble(GObject::getDrawPriority)).iterator();
			while (iterator.hasNext()) {
				GObject object = iterator.next();
				if (!object.isLoaded()) continue;
				long now = System.currentTimeMillis();
				if (object.isVisible()) object.draw(this);
				now = System.currentTimeMillis() - now;
				if (now > 10) System.out.println("[WARN DRAW] " + object + " took " + now + " millis!");
			}
			Engine.getEventManager().callEvent(new AfterDrawEvent(dif));
		} catch (Exception ex) {
			System.err.println("An error has occurred while Processing was drawing!");
			ex.printStackTrace();
		}
	}

	private void onChildrenTick(Parent parent, long dif) {
		parent.getChildren().stream().sorted(Comparator.comparingDouble(GObject::getDrawPriority)).forEach(child -> {
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
		KeyPressEvent keyEvent = new KeyPressEvent(EnumKeyboardKey.getById(event.getKeyCode()).orElse(EnumKeyboardKey.UNDEFINED), event.getKey(), event.getKeyCode());
		Engine.getEventManager().callEvent(keyEvent);
	}

	@Override
	public void keyReleased(KeyEvent event) {
		KeyReleaseEvent keyEvent = new KeyReleaseEvent(EnumKeyboardKey.getById(event.getKeyCode()).orElse(EnumKeyboardKey.UNDEFINED), event.getKey(), event.getKeyCode
			());
		Engine.getEventManager().callEvent(keyEvent);
	}

	@Override
	public void keyTyped(KeyEvent event) {
		KeyTypeEvent keyEvent = new KeyTypeEvent(event.getKey());
		Engine.getEventManager().callEvent(keyEvent);
	}
}
