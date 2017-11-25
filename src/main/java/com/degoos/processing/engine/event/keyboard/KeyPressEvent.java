package com.degoos.processing.engine.event.keyboard;

import com.degoos.processing.engine.enums.EnumKeyboardKey;

public class KeyPressEvent extends KeyEvent {

	private EnumKeyboardKey keyCode;

	public KeyPressEvent(EnumKeyboardKey keyCode, char key) {
		super(key);
		this.keyCode = keyCode;
	}

	public EnumKeyboardKey getKeyCode() {
		return keyCode;
	}
}
