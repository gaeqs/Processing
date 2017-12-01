package com.degoos.processing.engine.util;

import com.degoos.processing.engine.Engine;
import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2f;

public class CoordinatesUtils {

	public static Vector2f toProcessingCoordinates(Vector2d vector) {
		return toProcessingCoordinates(vector, true);
	}

	public static Vector2f toProcessingCoordinates(Vector2d vector, boolean subHeight) {
		return new Vector2f(vector.getX() * Engine.getCore().width, (subHeight ? Engine.getCore().height : 0) - (vector.getY() * Engine.getCore().height));
	}

}
