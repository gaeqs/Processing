package com.degoos.processing.game.manager;

import com.degoos.processing.engine.object.GObject;
import com.degoos.processing.game.entity.Entity;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EntityManager {

	private Map<Integer, Entity> entities;

	public EntityManager() {
		entities = new HashMap<>();
	}

	public Collection<Entity> getEntities() {
		return entities.values();
	}

	public int addEntity(Entity entity) {
		int id = getFirstAvailableId();
		entities.put(id, entity);
		return id;
	}

	public int addEntity(int id, Entity entity) {
		entities.put(id, entity);
		return id;
	}

	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}

	public void destroyAllEntities() {
		new HashMap<>(entities).values().forEach(GObject::delete);
	}

	public int getFirstAvailableId() {
		for (int i = 0; ; i++)
			if (!entities.containsKey(i)) return i;
	}
}
