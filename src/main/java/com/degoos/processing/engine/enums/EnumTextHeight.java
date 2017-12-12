package com.degoos.processing.engine.enums;

import com.degoos.processing.engine.Processing;
import java.util.Arrays;
import java.util.Optional;

public enum EnumTextHeight {

	BOTTOM(Processing.BOTTOM), CENTER(Processing.CENTER), TOP(Processing.TOP);

	private int id;

	EnumTextHeight(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static Optional<EnumTextHeight> getValueById(int id) {
		return Arrays.stream(values()).filter(target -> target.getId() == id).findAny();
	}

}
