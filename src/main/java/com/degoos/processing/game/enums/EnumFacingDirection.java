package com.degoos.processing.game.enums;

import com.flowpowered.math.vector.Vector2d;

public enum EnumFacingDirection {

	DOWN(0, -1, false),
	DOWN_RIGHT(1, -1, true),
	RIGHT(1, 0, false),
	UP_RIGHT(1, 1, true),
	UP(0, 1, false),
	UP_LEFT(-1, 1, true),
	LEFT(-1, 0, false),
	DOWN_LEFT(-1, -1, true);

	private String folderName;
	private Vector2d normalVector;
	private boolean diagonal;


	EnumFacingDirection(double x, double y, boolean diagonal) {
		folderName = name().toLowerCase();
		normalVector = new Vector2d(x, y);
		this.diagonal = diagonal;
	}

	EnumFacingDirection(String folderName, double x, double y, boolean diagonal) {
		this.folderName = folderName;
		normalVector = new Vector2d(x, y);
		this.diagonal = diagonal;
	}

	public String getFolderName() {
		return folderName;
	}

	public Vector2d getNormalVector() {
		return normalVector;
	}

	public boolean isDiagonal() {
		return diagonal;
	}

	public static EnumFacingDirection getFacingDirection(boolean up, boolean down, boolean left, boolean right) {
		if (up) {
			if (left) return UP_LEFT;
			if (right) return UP_RIGHT;
			return UP;
		}
		if (down) {
			if (left) return DOWN_LEFT;
			if (right) return DOWN_RIGHT;
			return DOWN;
		}
		if (left) return LEFT;
		if (right) return RIGHT;
		return null;
	}
}
