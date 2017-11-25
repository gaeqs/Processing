package com.degoos.processing.game.util;

import com.degoos.processing.game.entity.SavableEntity;
import com.degoos.processing.game.object.Level;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class SavableEntitiesUtils {

	public static void loadEntities(Level level, InputStream inputStream) {
		DataInputStream stream = new DataInputStream(inputStream);
		try {
			int size = stream.readInt();
			System.out.println(size);
			for (int i = 0; i < size; i++) {
				Class<?> clazz = Class.forName(stream.readUTF());
				clazz.getConstructor(DataInputStream.class, Level.class).newInstance(stream, level);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void saveEntities(Level level, OutputStream outputStream) {
		DataOutputStream stream = new DataOutputStream(outputStream);
		try {
			stream.writeInt(level.getLevelEntities().size());
			for (SavableEntity entity : level.getLevelEntities()) {
				stream.writeUTF(entity.getClass().getName());
				entity.save(stream);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
