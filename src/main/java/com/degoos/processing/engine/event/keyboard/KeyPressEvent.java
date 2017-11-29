package com.degoos.processing.engine.event.keyboard;

import com.degoos.processing.engine.enums.EnumKeyboardKey;

public class KeyPressEvent extends KeyEvent {

	private EnumKeyboardKey keyCode;
	private int keyId;

	public KeyPressEvent(EnumKeyboardKey keyCode, char key, int keyId) {
		super(key);
		this.keyCode = keyCode;
		this.keyId = keyId;
	}

	public EnumKeyboardKey getKeyCode() {
		return keyCode;
	}

	public int getKeyId() {
		return keyId;
	}
}
