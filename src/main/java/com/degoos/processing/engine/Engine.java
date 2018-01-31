package com.degoos.processing.engine;

import com.degoos.processing.engine.core.GObjectManager;
import com.degoos.processing.engine.enums.EnumTextureSampling;
import com.degoos.processing.engine.event.EventManager;
import com.degoos.processing.engine.sound.SoundManager;
import com.degoos.processing.engine.util.Validate;
import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2i;
import com.sun.javafx.application.PlatformImpl;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import processing.opengl.PGraphicsOpenGL;

public class Engine {

	private static Processing core;
	private static GObjectManager objectManager;
	private static EventManager eventManager;
	private static SoundManager soundManager;
	private static boolean started = false;

	public static void startEngine(Vector2i size) {
		eventManager = EventManager.getInstance();
		objectManager = new GObjectManager();
		soundManager = new SoundManager();
		started = true;
		Processing.startEngine(size);
		core = Processing.getInstance();
		PlatformImpl.startup(() -> {});
	}

	public static Processing getCore() {
		return core;
	}

	public static GObjectManager getObjectManager() {
		return objectManager;
	}

	public static EventManager getEventManager() {
		return eventManager;
	}

	public static SoundManager getSoundManager() {
		return soundManager;
	}

	public static Vector2d getMousePosition() {
		return new Vector2d(core.mouseX / (double) core.width, (core.height - core.mouseY) / (double) core.height);
	}

	public static EnumTextureSampling getTextureSampling() {
		if (core.g instanceof PGraphicsOpenGL) {
			try {
				Field field = PGraphicsOpenGL.class.getDeclaredField("textureSampling");
				field.setAccessible(true);
				int i = field.getInt(core.g);
				field.setAccessible(false);
				return EnumTextureSampling.getById(i).orElse(EnumTextureSampling.NEAREST);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return EnumTextureSampling.NEAREST;
	}

	public static void setTextureSampling(EnumTextureSampling sampling) {
		Validate.notNull(sampling, "Sampling cannot be null!");
		if (core.g instanceof PGraphicsOpenGL) {
			((PGraphicsOpenGL) core.g).textureSampling(sampling.getId());
		}
	}

	public static boolean isStarted() {
		return started;
	}

	public static URL getResourceURL(String filename) {
		if (filename == null) {
			throw new IllegalArgumentException("Filename cannot be null");
		} else {
			return Engine.class.getClassLoader().getResource(filename);
		}
	}

	public static URLConnection getResourceURLConnection(String filename) {
		if (filename == null) {
			throw new IllegalArgumentException("Filename cannot be null");
		} else {
			try {
				URL url = getResourceURL(filename);
				if (url == null) {
					return null;
				} else {
					URLConnection connection = url.openConnection();
					connection.setUseCaches(false);
					return connection;
				}
			} catch (IOException var4) {
				return null;
			}
		}
	}


	public static InputStream getResourceInputStream(String filename) {
		try {
			return getResourceURLConnection(filename).getInputStream();
		} catch (Exception ex) {
			return null;
		}
	}

	public static OutputStream getResourceOutputStream(String filename) {
		try {
			return getResourceURLConnection(filename).getOutputStream();
		} catch (Exception ex) {
			return null;
		}
	}
}
