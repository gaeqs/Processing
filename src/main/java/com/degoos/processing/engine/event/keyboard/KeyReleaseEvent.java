package com.degoos.processing.engine.event.keyboard;

import com.degoos.processing.engine.enums.EnumKeyboardKey;

public class KeyReleaseEvent extends KeyEvent {

	private EnumKeyboardKey keyCode;

	public KeyReleaseEvent(EnumKeyboardKey keyCode, char key) {
		super(key);
		this.keyCode = keyCode;
	}

	public EnumKeyboardKey getKeyCode() {
		return keyCode;
	}
}
