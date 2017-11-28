package com.degoos.processing.engine.enums;

import com.degoos.processing.engine.Processing;

public enum EnumArcMode {

	OPEN(Processing.OPEN), CHORD(Processing.CHORD), PIE(Processing.PIE);

	private int id;

	EnumArcMode(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
