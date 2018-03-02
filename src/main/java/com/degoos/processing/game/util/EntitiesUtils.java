package com.degoos.processing.game.util;

import com.degoos.processing.game.entity.Entity;
import com.degoos.processing.game.entity.SavableEntity;
import com.degoos.processing.game.object.Level;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class EntitiesUtils {

	public static void loadSavableEntities(Level level, InputStream inputStream) {
		DataInputStream stream = new DataInputStream(inputStream);
		try {
			int size = stream.readInt();
			for (int i = 0; i < size; i++) {
				loadEntity(stream, level);
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

	public static Entity loadEntity(DataInput stream, Level level) {
		try {
			Class<?> clazz = Class.forName(stream.readUTF());
			if (level == null) return (Entity) clazz.getConstructor(DataInput.class).newInstance(stream);
			else return (Entity) clazz.getConstructor(DataInput.class, Level.class).newInstance(stream, level);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void saveEntities(Level level, OutputStream outputStream) {
		DataOutputStream stream = new DataOutputStream(outputStream);
		try {
			stream.writeInt(level.getLevelEntities().size());
			for (SavableEntity entity : level.getLevelEntities()) {
				saveEntity(stream, entity);
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


	public static void saveEntity(DataOutput stream, Entity entity) {
		try {
			stream.writeUTF(entity.getClass().getName());
			entity.save(stream);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
