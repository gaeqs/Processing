package com.degoos.processing.game.object;

import com.degoos.processing.game.enums.EnumCollisionFace;
import com.flowpowered.math.vector.Vector2d;

public class Collision {

	private EnumCollisionFace collisionFace;
	private Vector2d point;

	public Collision(EnumCollisionFace collisionFace, Vector2d point) {
		this.collisionFace = collisionFace;
		this.point = point;
	}

	public EnumCollisionFace getCollisionFace() {
		return collisionFace;
	}

	public void setCollisionFace(EnumCollisionFace collisionFace) {
		this.collisionFace = collisionFace;
	}

	public Vector2d getPoint() {
		return point;
	}

	public void setPoint(Vector2d point) {
		this.point = point;
	}
}
