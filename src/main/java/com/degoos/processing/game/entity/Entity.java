package com.degoos.processing.game.entity;

import com.degoos.processing.engine.object.Shape;
import com.degoos.processing.engine.util.Validate;
import com.degoos.processing.game.Game;
import com.degoos.processing.game.controller.Controller;
import com.degoos.processing.game.object.Area;
import com.degoos.processing.game.object.Camera;
import com.degoos.processing.game.util.GameCoordinatesUtils;
import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2i;

public class Entity extends Shape {

	private Vector2d position;
	private Area relativeCollisionBox, currentCollisionBox, relativeDisplayArea, currentDisplayArea;
	private Controller controller;
	private boolean tangible;
	private double velocity;


	public Entity(Vector2d position, Area relativeCollisionBox, Area relativeDisplayArea, boolean tangible, double velocity, Controller controller) {
		super(true, 1, 2, new Vector2d(0, 0));
		Validate.notNull(position, "Position cannot be null!");
		Validate.notNull(relativeCollisionBox, "Relative collision box cannot be null!");
		Validate.notNull(relativeDisplayArea, "Relative display area cannot be null!");
		this.position = position;
		this.relativeCollisionBox = relativeCollisionBox;
		this.relativeDisplayArea = relativeDisplayArea;
		this.tangible = tangible;
		this.velocity = velocity;
		this.controller = controller;
		recalculateAreas();
		Game.getEntityManager().addEntity(this);
	}

	public void setCurrentCollisionBox(Area currentCollisionBox) {
		this.currentCollisionBox = currentCollisionBox;
	}

	public Vector2d getPosition() {
		return position;
	}

	public void setPosition(Vector2d position) {
		Validate.notNull(position, "Position cannot be null!");
		this.position = position;
		recalculateAreas();
	}

	public Area getRelativeCollisionBox() {
		return relativeCollisionBox;
	}

	public void setRelativeCollisionBox(Area relativeCollisionBox) {
		Validate.notNull(relativeCollisionBox, "Relative collision box cannot be null!");
		this.relativeCollisionBox = relativeCollisionBox;
		recalculateAreas();
	}

	public Area getCurrentCollisionBox() {
		return currentCollisionBox;
	}

	public Area getRelativeDisplayArea() {
		return relativeDisplayArea;
	}

	public void setRelativeDisplayArea(Area relativeDisplayArea) {
		Validate.notNull(relativeDisplayArea, "Relative display area cannot be null!");
		this.relativeDisplayArea = relativeDisplayArea;
		recalculateAreas();
	}

	public Area getCurrentDisplayArea() {
		return currentDisplayArea;
	}

	public boolean isTangible() {
		return tangible;
	}

	public void setTangible(boolean tangible) {
		this.tangible = tangible;
	}

	public void setCurrentDisplayArea(Area currentDisplayArea) {
		this.currentDisplayArea = currentDisplayArea;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public void triggerMove(boolean up, boolean down, boolean left, boolean right, long dif) {
		double vel = velocity * dif;
		if (left && right) left = right = false;
		if (up && down) up = down = false;
		if (left) {
			move(-vel, 0);
		}
		if (right) {
			move(vel, 0);
		}
		if (up) {
			move(0, vel);
		}
		if (down) {
			move(0, -vel);
		}
	}

	public void move(Vector2d vector2d) {
		move(vector2d.getX(), vector2d.getY());
	}

	public void move(double x, double y) {
		Vector2d newPosition = position.add(x, y);

		Area newArea = new Area(getCurrentCollisionBox().getMin().add(x, y), getCurrentCollisionBox().getMax().add(x, y));
		for (Entity entity : Game.getEntityManager().getEntities()) {
			if (entity.equals(this)) continue;
			if (entity.getCurrentCollisionBox().collide(newArea)) {
				if (!entity.collide(this) && entity.isTangible() && isTangible()) return;
				if (!collide(entity) && entity.isTangible() && isTangible()) return;
			}
		}

		if (this.isTangible() && Game.getMap() != null) for (Area area : Game.getMap().getCollisionBoxes())
			if (area.collide(newArea)) return;

		position = newPosition;
		recalculateAreas();
	}

	private void recalculateAreas() {
		this.currentCollisionBox = new Area(relativeCollisionBox.getMin().add(position), relativeCollisionBox.getMax().add(position));
		this.currentDisplayArea = new Area(relativeDisplayArea.getMin().add(position), relativeDisplayArea.getMax().add(position));
	}

	public boolean collide(Entity entity) {
		return !this.isTangible() || !entity.isTangible();
	}

	@Override
	public void onTick(long dif) {
		if (controller != null) controller.onTick(dif, this);
		Camera camera = Game.getCamera();
		if (!camera.isVisible(this)) {
			setVisible(false);
			return;
		} else setVisible(true);
		getVertexes().clear();
		setOrigin(GameCoordinatesUtils.toEngineCoordinates(position));
		Vector2d cameraMin = GameCoordinatesUtils.toEngineCoordinates(relativeDisplayArea.getMin(), false);
		Vector2d cameraMax = GameCoordinatesUtils.toEngineCoordinates(relativeDisplayArea.getMax(), false);
		addVertexWithUv(cameraMin, new Vector2i(0, 0));
		addVertexWithUv(new Vector2d(cameraMax.getX(), cameraMin.getY()), new Vector2i(1, 0));
		addVertexWithUv(cameraMax, new Vector2i(1, 1));
		addVertexWithUv(new Vector2d(cameraMin.getX(), cameraMax.getY()), new Vector2i(0, 1));
	}

	@Override
	public void delete() {
		Game.getEntityManager().removeEntity(this);
		super.delete();
	}
}
