package com.degoos.processing.engine.core;

import com.degoos.processing.engine.object.GObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GObjectManager {

	private Map<Integer, GObject> objects;

	public GObjectManager() {
		objects = new HashMap<>();
	}

	public Collection<GObject> getObjects() {
		return objects.values();
	}

	public int addGObject(GObject object) {
		int id = getFirstAvailableId();
		objects.put(id, object);
		return id;
	}

	public void removeObject(GObject object) {
		objects.remove(object.getId());
	}

	public void removeObject(int id) {
		objects.remove(id);
	}

	public int getFirstAvailableId() {
		for (int i = 0; ; i++)
			if (!objects.containsKey(i)) return i;
	}
}
