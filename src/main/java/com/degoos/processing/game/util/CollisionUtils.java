package com.degoos.processing.game.util;

import com.degoos.processing.game.object.Area;
import com.flowpowered.math.vector.Vector2d;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class CollisionUtils {


	public static List<Area> loadCollisionBoxes(InputStream inputStream) {
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		List<Area> list = new ArrayList<>();
		try {
			int size = dataInputStream.readInt();
			for (int i = 0; i < size; i++) {
				Vector2d min = new Vector2d(dataInputStream.readDouble(), dataInputStream.readDouble());
				Vector2d max = new Vector2d(dataInputStream.readDouble(), dataInputStream.readDouble());
				list.add(new Area(min, max));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				dataInputStream.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return list;
	}

	public static void saveCollisionBoxes(OutputStream outputStream, List<Area> list) {
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		try {
			dataOutputStream.writeInt(list.size());
			for (Area area : list) {
				dataOutputStream.writeDouble(area.getMin().getX());
				dataOutputStream.writeDouble(area.getMin().getY());
				dataOutputStream.writeDouble(area.getMax().getX());
				dataOutputStream.writeDouble(area.getMax().getY());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				dataOutputStream.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
