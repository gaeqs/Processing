package com.degoos.processing.engine.object;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.util.FontUtils;
import com.degoos.processing.engine.util.Validate;
import com.flowpowered.math.vector.Vector2d;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import processing.core.PFont;

public class Font {

	private PFont handled;
	private java.awt.Font nativeFont;
	private FontRenderContext fontRenderContext;

	public Font(String path) {
		Validate.notNull(path, "Path cannot be null!");
		handled = Engine.getCore().loadFont(path);
		nativeFont = (java.awt.Font) handled.getNative();
	}

	public Font(InputStream inputStream, String extension) {
		Validate.notNull(inputStream, "InputStream cannot be null!");
		try {
			if (extension.toLowerCase().equals("vlw")) {
				handled = new PFont(inputStream);
			} else handled = FontUtils.createFont(inputStream, 100, false, null);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		nativeFont = handled == null ? null : (java.awt.Font) handled.getNative();
		fontRenderContext = new FontRenderContext(new AffineTransform(), true, true);
	}

	public Font(PFont font) {
		Validate.notNull(font, "Font cannot be null!");
		this.handled = font;
	}

	public Vector2d getStringSize(String string, double size) {
		if (nativeFont == null) return new Vector2d();
		Rectangle2D rectangle2D = nativeFont.getStringBounds(string, fontRenderContext);
		return new Vector2d(
			rectangle2D.getWidth() * size / (Engine.getCore().width * nativeFont.getSize2D()),
			rectangle2D.getHeight() * size / (Engine.getCore().height * nativeFont.getSize2D()));
	}

	public PFont getHandled() {
		return handled;
	}

}
