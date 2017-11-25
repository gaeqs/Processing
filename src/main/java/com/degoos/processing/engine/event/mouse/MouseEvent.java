package com.degoos.processing.engine.event.mouse;

import com.degoos.processing.engine.enums.EnumMouseKey;
import com.degoos.processing.engine.event.GEvent;
import com.flowpowered.math.vector.Vector2d;

public class MouseEvent extends GEvent {

	private Vector2d position;
	private EnumMouseKey pressedKey;
	private int count;

	public MouseEvent(Vector2d position, EnumMouseKey pressedKey, int count) {
		this.position = position;
		this.pressedKey = pressedKey;
		this.count = count;
	}

	public Vector2d getPosition() {
		return position;
	}

	public EnumMouseKey getPressedKey() {
		return pressedKey;
	}

	public int getCount() {
		return count;
	}
}
