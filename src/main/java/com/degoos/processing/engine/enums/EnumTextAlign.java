package com.degoos.processing.engine.enums;

import com.degoos.processing.engine.Processing;
import java.util.Arrays;
import java.util.Optional;

public enum EnumTextAlign {

	RIGHT(Processing.RIGHT), CENTER(Processing.CENTER), LEFT(Processing.LEFT);

	private int id;

	EnumTextAlign(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static Optional<EnumTextAlign> getValueById(int id) {
		return Arrays.stream(values()).filter(target -> target.getId() == id).findAny();
	}

}
