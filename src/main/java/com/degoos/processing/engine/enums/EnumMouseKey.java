package com.degoos.processing.engine.enums;

import java.util.Arrays;
import java.util.Optional;

public enum EnumMouseKey {

	PRIMARY(37), SECONDARY(39), TERTIARY(3), UNKNOWN(-1);

	private int id;

	EnumMouseKey(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static Optional<EnumMouseKey> getValueById(int id) {
		return Arrays.stream(values()).filter(target -> target.getId() == id).findAny();
	}
}
