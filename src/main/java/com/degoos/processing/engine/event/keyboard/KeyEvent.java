package com.degoos.processing.engine.event.keyboard;

import com.degoos.processing.engine.event.GEvent;

public class KeyEvent extends GEvent {

	private char key;

	public KeyEvent(char key) {
		this.key = key;
	}

	public char getKey() {
		return key;
	}
}
