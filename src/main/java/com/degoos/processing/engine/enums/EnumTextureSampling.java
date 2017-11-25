package com.degoos.processing.engine.enums;

import java.util.Arrays;
import java.util.Optional;

public enum EnumTextureSampling {

	NEAREST(2), LINEAR(3), BILINEAR(4), TRILINEAR(5);

	private int id;

	EnumTextureSampling(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static Optional<EnumTextureSampling> getById(int id) {
		return Arrays.stream(values()).filter(target -> target.getId() == id).findAny();
	}
}
