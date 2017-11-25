package com.degoos.processing.engine.event.mouse;

import com.degoos.processing.engine.enums.EnumMouseKey;
import com.flowpowered.math.vector.Vector2d;

public class MouseDragEvent extends MouseEvent {

	public MouseDragEvent(Vector2d position, EnumMouseKey pressedKey, int count) {
		super(position, pressedKey, count);
	}
}
