package com.degoos.processing.game.util;

import com.degoos.processing.game.object.Area;
import com.flowpowered.math.vector.Vector2d;
import java.io.DataOutputStream;
import java.io.IOException;

public class StreamUtils {

	public static void writeVector(DataOutputStream stream, Vector2d vector2d) throws IOException {
		stream.writeDouble(vector2d.getX());
		stream.writeDouble(vector2d.getY());
	}

	public static void writeArea(DataOutputStream stream, Area area) throws IOException {
		writeVector(stream, area.getMin());
		writeVector(stream, area.getMax());
	}

}
