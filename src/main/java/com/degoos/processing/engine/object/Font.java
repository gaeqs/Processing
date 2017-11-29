package com.degoos.processing.engine.object;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.util.Validate;
import java.io.IOException;
import java.io.InputStream;
import processing.core.PFont;

public class Font {

	private PFont handled;

	public Font(String path) {
		Validate.notNull(path, "Path cannot be null!");
		handled = Engine.getCore().loadFont(path);
	}

	public Font(InputStream inputStream) {
		Validate.notNull(inputStream, "InputStream cannot be null!");
		try {
			handled = new PFont(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Font(PFont font) {
		Validate.notNull(font, "Font cannot be null!");
		this.handled = font;
	}

	public PFont getHandled() {
		return handled;
	}

}
