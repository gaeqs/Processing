package com.degoos.processing.engine.util;

import com.degoos.processing.engine.Processing;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import processing.core.PApplet;
import processing.core.PImage;

public class ImageUtils {

	static byte TIFF_HEADER[] = {77, 77, 0, 42, 0, 0, 0, 8, 0, 9, 0, -2, 0, 4, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 3, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 3, 0, 0, 0, 1, 0, 0,
		0, 0, 1, 2, 0, 3, 0, 0, 0, 3, 0, 0, 0, 122, 1, 6, 0, 3, 0, 0, 0, 1, 0, 2, 0, 0, 1, 17, 0, 4, 0, 0, 0, 1, 0, 0, 3, 0, 1, 21, 0, 3, 0, 0, 0, 1, 0, 3, 0, 0, 1, 22,
		0, 3, 0, 0, 0, 1, 0, 0, 0, 0, 1, 23, 0, 4, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 8, 0, 8};

	public static PImage loadImage(InputStream stream, String extension) { //, Object params) {
		Validate.notNull(extension, "Extension cannot be null!");
		Processing instance = Processing.getInstance();
		// await... has to run on the main thread, because P2D and P3D call GL functions
		// If this runs on background, requestImage() already called await... on the main thread

		// just in case. them users will try anything!
		extension = extension.toLowerCase();

		if (extension.equals("tga")) {
			try {
				return loadImageTGA(stream);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		if (extension.equals("tif") || extension.equals("tiff")) {
			byte bytes[] = PApplet.loadBytes(stream);
			return (bytes == null) ? null : loadTIFF(bytes);
		}

		// For jpeg, gif, and png, load them using createImage(),
		// because the javax.imageio code was found to be much slower.
		// http://dev.processing.org/bugs/show_bug.cgi?id=392
		try {
			if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("gif") || extension.equals("png") || extension.equals("unknown")) {
				byte bytes[] = PApplet.loadBytes(stream);
				if (bytes == null) {
					return null;
				} else {
					//Image awtImage = Toolkit.getDefaultToolkit().createImage(bytes);
					Image awtImage = new ImageIcon(bytes).getImage();

					if (awtImage instanceof BufferedImage) {
						BufferedImage buffImage = (BufferedImage) awtImage;
						int space = buffImage.getColorModel().getColorSpace().getType();
						if (space == ColorSpace.TYPE_CMYK) {
							System.err.println(stream + " is a CMYK image, " + "only RGB images are supported.");
							return null;
			  /*
			  // wishful thinking, appears to not be supported
              // https://community.oracle.com/thread/1272045?start=0&tstart=0
              BufferedImage destImage =
                new BufferedImage(buffImage.getWidth(),
                                  buffImage.getHeight(),
                                  BufferedImage.TYPE_3BYTE_BGR);
              ColorConvertOp op = new ColorConvertOp(null);
              op.filter(buffImage, destImage);
              image = new PImage(destImage);
              */
						}
					}

					PImage image = new PImage(awtImage);
					if (image.width == -1) {
						System.err.println("The stream " + stream + " contains bad image data, or may not be an image.");
					}

					// if it's a .gif image, test to see if it has transparency
					if (extension.equals("gif") || extension.equals("png") || extension.equals("unknown")) {
						checkAlpha(image);
					}

					//          if (params != null) {
					//            image.setParams(g, params);
					//          }
					image.parent = instance;
					return image;
				}
			}
		} catch (Exception e) {
			// show error, but move on to the stuff below, see if it'll work
			e.printStackTrace();
		}

		String[] loadImageFormats = ImageIO.getReaderFormatNames();
		if (loadImageFormats != null) {
			for (int i = 0; i < loadImageFormats.length; i++) {
				if (extension.equals(loadImageFormats[i])) {
					return loadImageIO(stream);
					//          PImage image = loadImageIO(filename);
					//          if (params != null) {
					//            image.setParams(g, params);
					//          }
					//          return image;
				}
			}
		}

		// failed, could not load image after all those attempts
		System.err.println("Could not find a method to load " + stream);
		return null;
	}


	protected static PImage loadImageTGA(InputStream is) throws IOException {
		Processing instance = Processing.getInstance();
		if (is == null) return null;

		byte header[] = new byte[18];
		int offset = 0;
		do {
			int count = is.read(header, offset, header.length - offset);
			if (count == -1) return null;
			offset += count;
		} while (offset < 18);

    /*
      header[2] image type code
      2  (0x02) - Uncompressed, RGB images.
      3  (0x03) - Uncompressed, black and white images.
      10 (0x0A) - Run-length encoded RGB images.
      11 (0x0B) - Compressed, black and white images. (grayscale?)

      header[16] is the bit depth (8, 24, 32)

      header[17] image descriptor (packed bits)
      0x20 is 32 = origin upper-left
      0x28 is 32 + 8 = origin upper-left + 32 bits

        7  6  5  4  3  2  1  0
      128 64 32 16  8  4  2  1
    */

		int format = 0;

		if (((header[2] == 3) || (header[2] == 11)) &&  // B&W, plus RLE or not
			(header[16] == 8) &&  // 8 bits
			((header[17] == 0x8) || (header[17] == 0x28))) {  // origin, 32 bit
			format = 4;

		} else if (((header[2] == 2) || (header[2] == 10)) &&  // RGB, RLE or not
			(header[16] == 24) &&  // 24 bits
			((header[17] == 0x20) || (header[17] == 0))) {  // origin
			format = 1;

		} else if (((header[2] == 2) || (header[2] == 10)) && (header[16] == 32) && ((header[17] == 0x8) || (header[17] == 0x28))) {  // origin, 32
			format = 2;
		}

		if (format == 0) {
			System.err.println("Unknown .tga file format for " + is);
			//" (" + header[2] + " " +
			//(header[16] & 0xff) + " " +
			//hex(header[17], 2) + ")");
			return null;
		}

		int w = ((header[13] & 0xff) << 8) + (header[12] & 0xff);
		int h = ((header[15] & 0xff) << 8) + (header[14] & 0xff);
		PImage outgoing = instance.createImage(w, h, format);

		// where "reversed" means upper-left corner (normal for most of
		// the modernized world, but "reversed" for the tga spec)
		//boolean reversed = (header[17] & 0x20) != 0;
		// https://github.com/processing/processing/issues/1682
		boolean reversed = (header[17] & 0x20) == 0;

		if ((header[2] == 2) || (header[2] == 3)) {  // not RLE encoded
			if (reversed) {
				int index = (h - 1) * w;
				switch (format) {
					case 4:
						for (int y = h - 1; y >= 0; y--) {
							for (int x = 0; x < w; x++) {
								outgoing.pixels[index + x] = is.read();
							}
							index -= w;
						}
						break;
					case 1:
						for (int y = h - 1; y >= 0; y--) {
							for (int x = 0; x < w; x++) {
								outgoing.pixels[index + x] = is.read() | (is.read() << 8) | (is.read() << 16) | 0xff000000;
							}
							index -= w;
						}
						break;
					case 2:
						for (int y = h - 1; y >= 0; y--) {
							for (int x = 0; x < w; x++) {
								outgoing.pixels[index + x] = is.read() | (is.read() << 8) | (is.read() << 16) | (is.read() << 24);
							}
							index -= w;
						}
				}
			} else {  // not reversed
				int count = w * h;
				switch (format) {
					case 4:
						for (int i = 0; i < count; i++) {
							outgoing.pixels[i] = is.read();
						}
						break;
					case 1:
						for (int i = 0; i < count; i++) {
							outgoing.pixels[i] = is.read() | (is.read() << 8) | (is.read() << 16) | 0xff000000;
						}
						break;
					case 2:
						for (int i = 0; i < count; i++) {
							outgoing.pixels[i] = is.read() | (is.read() << 8) | (is.read() << 16) | (is.read() << 24);
						}
						break;
				}
			}

		} else {  // header[2] is 10 or 11
			int index = 0;
			int px[] = outgoing.pixels;

			while (index < px.length) {
				int num = is.read();
				boolean isRLE = (num & 0x80) != 0;
				if (isRLE) {
					num -= 127;  // (num & 0x7F) + 1
					int pixel = 0;
					switch (format) {
						case 4:
							pixel = is.read();
							break;
						case 1:
							pixel = 0xFF000000 | is.read() | (is.read() << 8) | (is.read() << 16);
							//(is.read() << 16) | (is.read() << 8) | is.read();
							break;
						case 2:
							pixel = is.read() | (is.read() << 8) | (is.read() << 16) | (is.read() << 24);
							break;
					}
					for (int i = 0; i < num; i++) {
						px[index++] = pixel;
						if (index == px.length) break;
					}
				} else {  // write up to 127 bytes as uncompressed
					num += 1;
					switch (format) {
						case 4:
							for (int i = 0; i < num; i++) {
								px[index++] = is.read();
							}
							break;
						case 1:
							for (int i = 0; i < num; i++) {
								px[index++] = 0xFF000000 | is.read() | (is.read() << 8) | (is.read() << 16);
								//(is.read() << 16) | (is.read() << 8) | is.read();
							}
							break;
						case 2:
							for (int i = 0; i < num; i++) {
								px[index++] = is.read() | //(is.read() << 24) |
									(is.read() << 8) | (is.read() << 16) | (is.read() << 24);
								//(is.read() << 16) | (is.read() << 8) | is.read();
							}
							break;
					}
				}
			}

			if (!reversed) {
				int[] temp = new int[w];
				for (int y = 0; y < h / 2; y++) {
					int z = (h - 1) - y;
					System.arraycopy(px, y * w, temp, 0, w);
					System.arraycopy(px, z * w, px, y * w, w);
					System.arraycopy(temp, 0, px, z * w, w);
				}
			}
		}
		is.close();
		return outgoing;
	}

	public static PImage loadTIFF(byte tiff[]) {
		if ((tiff[42] != tiff[102]) ||  // width/height in both places
			(tiff[43] != tiff[103])) {
			System.err.println("Error: Processing can only read its own TIFF files.");
			return null;
		}

		int width = ((tiff[30] & 0xff) << 8) | (tiff[31] & 0xff);
		int height = ((tiff[42] & 0xff) << 8) | (tiff[43] & 0xff);

		int count = ((tiff[114] & 0xff) << 24) | ((tiff[115] & 0xff) << 16) | ((tiff[116] & 0xff) << 8) | (tiff[117] & 0xff);
		if (count != width * height * 3) {
			System.err.println("Error: Processing can only read its own TIFF files." + " (" + width + ", " + height + ")");
			return null;
		}

		// check the rest of the header
		for (int i = 0; i < TIFF_HEADER.length; i++) {
			if ((i == 30) || (i == 31) || (i == 42) || (i == 43) || (i == 102) || (i == 103) || (i == 114) || (i == 115) || (i == 116) || (i == 117)) continue;

			if (tiff[i] != TIFF_HEADER[i]) {
				System.err.println("Error: Processing can only read its own TIFF files." + " (" + i + ")");
				return null;
			}
		}

		PImage outgoing = new PImage(width, height, 1);
		int index = 768;
		count /= 3;
		for (int i = 0; i < count; i++) {
			outgoing.pixels[i] = 0xFF000000 | (tiff[index++] & 0xff) << 16 | (tiff[index++] & 0xff) << 8 | (tiff[index++] & 0xff);
		}
		return outgoing;
	}

	public static PImage loadImageIO(InputStream stream) {
		if (stream == null) {
			System.err.println("The image " + stream + " could not be found.");
			return null;
		}

		try {
			BufferedImage bi = ImageIO.read(stream);
			PImage outgoing = new PImage(bi.getWidth(), bi.getHeight());
			outgoing.parent = Processing.getInstance();

			bi.getRGB(0, 0, outgoing.width, outgoing.height, outgoing.pixels, 0, outgoing.width);

			// check the alpha for this image
			// was gonna call getType() on the image to see if RGB or ARGB,
			// but it's not actually useful, since gif images will come through
			// as TYPE_BYTE_INDEXED, which means it'll still have to check for
			// the transparency. also, would have to iterate through all the other
			// types and guess whether alpha was in there, so.. just gonna stick
			// with the old method.
			checkAlpha(outgoing);

			stream.close();
			// return the image
			return outgoing;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void checkAlpha(PImage image) {
		try {
			Method method = image.getClass().getDeclaredMethod("checkAlpha");
			method.setAccessible(true);
			method.invoke(image);
			method.setAccessible(false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
