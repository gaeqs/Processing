package com.degoos.processing.engine.util;

import com.degoos.processing.engine.Engine;
import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2f;

public class CoordinatesUtils {

	public static Vector2f transformIntoProcessingCoordinates(Vector2d vector) {
		return new Vector2f(vector.getX() * Engine.getCore().width, Engine.getCore().height - (vector.getY() * Engine.getCore().height));
	}

}
