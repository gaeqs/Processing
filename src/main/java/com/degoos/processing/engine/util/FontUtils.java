package com.degoos.processing.engine.util;

import com.degoos.processing.engine.Processing;
import java.awt.Font;
import java.io.InputStream;
import processing.core.PFont;

public class FontUtils {

	public static PFont createFont(InputStream stream, float size, boolean smooth, char[] charset) {
		Font baseFont = null;
		try {
			if (stream == null) {
				System.err.println("The font \"" + stream + "\" " + "is missing or inaccessible, make sure " + "the URL is valid or that the file has been " +
					"added to your sketch and is readable.");
				return null;
			}
			baseFont = Font.createFont(Font.TRUETYPE_FONT, stream);
			return new PFont(baseFont.deriveFont(size * Processing.getInstance().pixelDensity), smooth, charset, true, Processing.getInstance().pixelDensity);

		} catch (Exception e) {
			System.err.println("Problem with createFont(\"" + stream + "\")");
			e.printStackTrace();
			return null;
		}
	}

}
