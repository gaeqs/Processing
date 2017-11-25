package com.degoos.processing.engine.object;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.util.Validate;
import processing.core.PFont;

public class Font {

	private String path;
	private PFont handled;

	public Font(String path) {
		Validate.notNull(path, "Path cannot be null!");
		this.path = path;
		handled = Engine.getCore().loadFont(path);
	}

	public Font(PFont font) {
		Validate.notNull(font, "Font cannot be null!");
		this.handled = font;
		this.path = font.getName();
	}


	public String getPath() {
		return path;
	}

	public PFont getHandled() {
		return handled;
	}

}
