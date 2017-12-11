package com.degoos.processing.engine.core;

import com.degoos.processing.engine.object.GObject;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GObjectManager {

	private Map<Integer, GObject> objects;

	public GObjectManager() {
		objects = new ConcurrentHashMap<>();
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
