package com.degoos.processing.engine.object;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.Processing;
import com.degoos.processing.engine.util.ImageUtils;
import com.degoos.processing.engine.util.Validate;
import java.io.InputStream;
import processing.core.PImage;

public class Image extends GObject {

	private PImage handled;

	public Image() {
	}

	public Image(String path) {
		Validate.notNull(path, "Path cannot be null!");
		handled = Engine.getCore().loadImage(path);
	}

	public Image(InputStream inputStream, String extension) {
		try {
			handled = ImageUtils.loadImage(inputStream, extension);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public PImage getHandled() {
		return handled;
	}

	@Override
	public void draw(Processing core) {
	}

	@Override
	public void onTick(long dif) {
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		Image image = (Image) o;

		return handled != null ? handled.equals(image.handled) : image.handled == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (handled != null ? handled.hashCode() : 0);
		return result;
	}
}
