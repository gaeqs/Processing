package com.degoos.processing.game.object;

public enum EnumFacingDirection {

	DOWN, DOWN_RIGHT, RIGHT, UP_RIGHT, UP, UP_LEFT, LEFT, DOWN_LEFT;

	private String folderName;

	EnumFacingDirection() {
		folderName = name().toLowerCase();
	}

	EnumFacingDirection(String folderName) {
		this.folderName = folderName;
	}

	public String getFolderName() {
		return folderName;
	}
}
