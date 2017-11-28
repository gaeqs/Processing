package com.degoos.processing.engine.event.screen;

import com.degoos.processing.engine.event.GEvent;
import com.degoos.processing.engine.util.Validate;
import com.flowpowered.math.vector.Vector2i;

public class ScreenResizeEvent extends GEvent {

	private Vector2i oldSize, newSize;

	public ScreenResizeEvent(Vector2i oldSize, Vector2i newSize) {
		Validate.notNull(newSize, "New size cannot be null!");
		Validate.notNull(oldSize, "Old size cannot be null!");
		this.oldSize = oldSize;
		this.newSize = newSize;
	}

	public Vector2i getOldSize() {
		return oldSize;
	}

	public Vector2i getNewSize() {
		return newSize;
	}

	public void setNewSize(Vector2i newSize) {
		Validate.notNull(newSize, "New size cannot be null!");
		this.newSize = newSize;
	}
}
