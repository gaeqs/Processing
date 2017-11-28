package com.degoos.processing.game.util;

import com.flowpowered.math.vector.Vector2d;
import java.awt.geom.Line2D;

public class LineUtils {

	public static Vector2d getIntersection(final Line2D.Double line1, final Line2D.Double line2) {
		final double x1, y1, x2, y2, x3, y3, x4, y4;
		x1 = line1.x1;
		y1 = line1.y1;
		x2 = line1.x2;
		y2 = line1.y2;
		x3 = line2.x1;
		y3 = line2.y1;
		x4 = line2.x2;
		y4 = line2.y2;
		final double x = ((x2 - x1) * (x3 * y4 - x4 * y3) - (x4 - x3) * (x1 * y2 - x2 * y1)) / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
		final double y = ((y3 - y4) * (x1 * y2 - x2 * y1) - (y1 - y2) * (x3 * y4 - x4 * y3)) / ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
		return new Vector2d(x, y);
	}

}
