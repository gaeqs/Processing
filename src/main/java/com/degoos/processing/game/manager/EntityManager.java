package com.degoos.processing.game.manager;

import com.degoos.processing.engine.object.GObject;
import com.degoos.processing.game.entity.Entity;
import java.util.HashSet;
import java.util.Set;

public class EntityManager {

	private Set<Entity> entities;

	public EntityManager() {
		entities = new HashSet<>();
	}

	public Set<Entity> getEntities() {
		return entities;
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}

	public void destroyAllEntities () {
		entities.forEach(GObject::delete);
	}
}
