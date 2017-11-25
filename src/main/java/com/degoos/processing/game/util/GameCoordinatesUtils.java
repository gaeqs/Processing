package com.degoos.processing.game.util;

import com.degoos.processing.game.Game;
import com.degoos.processing.game.object.Camera;
import com.flowpowered.math.vector.Vector2d;

public class GameCoordinatesUtils {

	public static Vector2d toEngineCoordinates(Vector2d vector) {
		Camera camera = Game.getCamera();
		return vector.sub(camera.getPosition()).add(camera.getXRadius(), camera.getYRadius()).div(camera.getXRadius() * 2, camera.getYRadius() * 2);
	}

	public static Vector2d toEngineCoordinates(Vector2d vector, boolean subPosition) {
		Camera camera = Game.getCamera();
		if (subPosition) return vector.sub(camera.getPosition()).add(camera.getXRadius(), camera.getYRadius()).div(camera.getXRadius() * 2, camera.getYRadius() * 2);
		else return vector.div(camera.getXRadius() * 2, camera.getYRadius() * 2);
	}

}
