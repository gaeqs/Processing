package com.degoos.processing.game.entity;

import com.degoos.processing.engine.Processing;
import com.degoos.processing.engine.object.Shape;
import com.degoos.processing.engine.util.CoordinatesUtils;
import com.degoos.processing.engine.util.Validate;
import com.degoos.processing.game.Game;
import com.degoos.processing.game.controller.Controller;
import com.degoos.processing.game.enums.EnumCollideAction;
import com.degoos.processing.game.enums.EnumCollisionFace;
import com.degoos.processing.game.listener.SetupListener;
import com.degoos.processing.game.network.packet.Packet;
import com.degoos.processing.game.network.packet.out.PacketOutEntityMove;
import com.degoos.processing.game.object.Area;
import com.degoos.processing.game.object.Camera;
import com.degoos.processing.game.object.Collision;
import com.degoos.processing.game.object.LinealArea;
import com.degoos.processing.game.object.ParallelogramArea;
import com.degoos.processing.game.util.GameCoordinatesUtils;
import com.degoos.processing.game.util.StreamUtils;
import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2f;
import com.flowpowered.math.vector.Vector2i;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entity extends Shape {

	private Vector2d position;
	private Area relativeCollisionBox, currentCollisionBox, relativeDisplayArea, currentDisplayArea;
	private Controller controller;
	private boolean tangible;
	private double velocity;
	private Vector2d point1, point2, point3, point4;
	private boolean canMove;
	private int entityId;


	public Entity(Vector2d position, Area relativeCollisionBox, Area relativeDisplayArea, boolean tangible, double velocity, boolean canMove, Controller controller) {
		this(-1, position, relativeCollisionBox, relativeDisplayArea, tangible, velocity, canMove, controller);
	}

	public Entity(int entityId, Vector2d position, Area relativeCollisionBox, Area relativeDisplayArea, boolean tangible, double velocity, boolean canMove,
		Controller controller) {
		super(true, Game.getLevel() == null ? 1 : Game.getLevel().getSize().getY() - position.getY(), 2, new Vector2d(0, 0));
		this.entityId = entityId < 0 ? Game.getEntityManager().addEntity(this) : Game.getEntityManager().addEntity(entityId, this);
		Validate.notNull(position, "Position cannot be null!");
		Validate.notNull(relativeCollisionBox, "Relative collision box cannot be null!");
		Validate.notNull(relativeDisplayArea, "Relative display area cannot be null!");
		this.position = position;
		this.relativeCollisionBox = relativeCollisionBox;
		this.relativeDisplayArea = relativeDisplayArea;
		this.tangible = tangible;
		this.velocity = velocity;
		this.controller = controller;
		this.canMove = canMove;
		recalculateAreas();
	}

	public Entity(DataInputStream inputStream) throws IOException {
		this(inputStream.readInt(), new Vector2d(inputStream.readDouble(), inputStream.readDouble()), new Area(new Vector2d(inputStream.readDouble(), inputStream
			.readDouble()), new Vector2d(inputStream.readDouble(), inputStream.readDouble())), new Area(new Vector2d(inputStream.readDouble(), inputStream
			.readDouble()), new Vector2d(inputStream.readDouble(), inputStream.readDouble())), inputStream.readBoolean(), inputStream.readDouble(), false, null);
		setPosition(getPosition());
	}

	public Entity(DataInputStream inputStream, Controller controller) throws IOException {
		this(inputStream.readInt(), new Vector2d(inputStream.readDouble(), inputStream.readDouble()), new Area(new Vector2d(inputStream.readDouble(), inputStream
			.readDouble()), new Vector2d(inputStream.readDouble(), inputStream.readDouble())), new Area(new Vector2d(inputStream.readDouble(), inputStream
			.readDouble()), new Vector2d(inputStream.readDouble(), inputStream.readDouble())), inputStream.readBoolean(), inputStream.readDouble(), false, controller);
		setPosition(getPosition());
	}

	public int getEntityId() {
		return entityId;
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
		setDrawPriority(Game.getLevel() == null ? 1 : Game.getLevel().getSize().getY() - position.getY());
		if (Game.isServer()) {
			Packet packet = new PacketOutEntityMove(getEntityId(), position);
			Game.getGameServer().getServerClients().forEach(client -> client.sendPacket(packet));
		}
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

	public boolean canMove() {
		return canMove;
	}

	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public void triggerMove(boolean up, boolean down, boolean left, boolean right, long dif) {
		if (!canMove) return;
		double vel = velocity * dif;
		if (left && right) left = right = false;
		if (up && down) up = down = false;
		Vector2d vector2d = new Vector2d();
		if (left) {
			vector2d = vector2d.add(-vel, 0);
		}
		if (right) {
			vector2d = vector2d.add(vel, 0);
		}
		if (up) {
			vector2d = vector2d.add(0, vel);
		}
		if (down) {
			vector2d = vector2d.add(0, -vel);
		}
		move(vector2d);
	}

	public void move(Vector2d vector2d) {
		move(vector2d.getX(), vector2d.getY());
	}

	public void move(double x, double y) {
		if (!canMove || (x == 0 && y == 0)) return;
		Vector2d newPosition = position.add(x, y);
		Area maxArea = new Area(getCurrentCollisionBox().getMin().add(x, y), getCurrentCollisionBox().getMax().add(x, y));

		boolean finalX = x == 0;
		boolean finalY = y == 0;
		point3 = null;
		calculatePoints(x, y, currentCollisionBox);
		Vector2d f1 = point1, f2 = point2, f3 = point3, f4 = point4;
		calculatePoints(x, y, maxArea);
		LinealArea centerLine = finalX || finalY ? null : new LinealArea(f4, point1);
		ParallelogramArea verticalArea = finalX ? null : new ParallelogramArea(f1, f2, point1, point2);
		ParallelogramArea horizontalArea = finalY ? null : new ParallelogramArea(f1, x == 0 ? f2 : f3, point1, x == 0 ? point2 : point3);

		List<Entity> verticalEntities = new ArrayList<>(), horizontalEntities = new ArrayList<>();
		Map<Entity, Collision> collisions = new HashMap<>();

		Game.getEntityManager().getEntities().forEach(entity -> {
			if (entity.equals(this)) return;
			boolean vertical = verticalArea != null && verticalArea.collide(entity.getCurrentCollisionBox());
			boolean horizontal = horizontalArea != null && horizontalArea.collide(entity.getCurrentCollisionBox());
			if (vertical && horizontal) {
				Collision collision = centerLine.getFirstCollisionPoint(entity.getCurrentCollisionBox());
				if (collision == null) {
					System.out.println("ERROR NULL?????");
					centerLine.getFirstCollisionPoint(entity.getCurrentCollisionBox(), true);
					return;
				}
				if (collision.getCollisionFace() == EnumCollisionFace.LEFT || collision.getCollisionFace() == EnumCollisionFace.RIGHT) verticalEntities.add(entity);
				else if (collision.getCollisionFace() == EnumCollisionFace.UP || collision.getCollisionFace() == EnumCollisionFace.DOWN) horizontalEntities.add(entity);
				collisions.put(entity, collision);
			} else if (vertical) verticalEntities.add(entity);
			else if (horizontal) horizontalEntities.add(entity);
		});
		if (verticalEntities.size() > 1) verticalEntities.sort(Comparator.comparingDouble(o -> Math.abs(o.getPosition().getX() - position.getX())));
		if (horizontalEntities.size() > 1) horizontalEntities.sort(Comparator.comparingDouble(o -> Math.abs(o.getPosition().getY() - position.getY())));
		for (Entity entity : verticalEntities) {
			EnumCollideAction action = entity.collide(this);
			if (action == EnumCollideAction.CANCEL) return;
			if (action == EnumCollideAction.COLLIDE) {
				double newX = collisions.containsKey(entity) ? collisions.get(entity).getPoint().getX()
				                                             : (x > 0 ? entity.getCurrentCollisionBox().getMin() : entity.getCurrentCollisionBox().getMax()).getX();
				newX -= x > 0 ? relativeCollisionBox.getMax().getX() : relativeCollisionBox.getMin().getX();
				newPosition = new Vector2d(newX, newPosition.getY());
				break;
			}
		}
		for (Entity entity : horizontalEntities) {
			EnumCollideAction action = entity.collide(this);
			if (action == EnumCollideAction.CANCEL) return;
			if (action == EnumCollideAction.COLLIDE) {
				double newY = collisions.containsKey(entity) ? collisions.get(entity).getPoint().getY()
				                                             : (y > 0 ? entity.getCurrentCollisionBox().getMin() : entity.getCurrentCollisionBox().getMax()).getY();
				newY -= y > 0 ? relativeCollisionBox.getMax().getY() : relativeCollisionBox.getMin().getY();
				newPosition = new Vector2d(newPosition.getX(), newY);
				break;
			}
		}
		setPosition(newPosition);
	}

	private void calculatePoints(double x, double y, Area area) {
		if (x > 0) {
			if (y > 0) {
				point1 = area.getMax();
				point2 = area.getMaxMin();
				point3 = area.getMinMax();
				point4 = area.getMin();
			} else if (y < 0) {
				point1 = area.getMaxMin();
				point2 = area.getMax();
				point3 = area.getMin();
				point4 = area.getMinMax();
			} else {
				point1 = area.getMax();
				point2 = area.getMaxMin();
			}
		} else if (x < 0) {
			if (y > 0) {
				point1 = area.getMinMax();
				point2 = area.getMin();
				point3 = area.getMax();
				point4 = area.getMaxMin();
			} else if (y < 0) {
				point1 = area.getMin();
				point2 = area.getMinMax();
				point3 = area.getMaxMin();
				point4 = area.getMax();
			} else {
				point1 = area.getMinMax();
				point2 = area.getMin();
			}
		} else {
			if (y > 0) {
				point1 = area.getMinMax();
				point2 = area.getMax();
			} else if (y < 0) {
				point1 = area.getMin();
				point2 = area.getMaxMin();
			}
		}
	}

	private void recalculateAreas() {
		this.currentCollisionBox = new Area(relativeCollisionBox.getMin().add(position), relativeCollisionBox.getMax().add(position));
		this.currentDisplayArea = new Area(relativeDisplayArea.getMin().add(position), relativeDisplayArea.getMax().add(position));
	}

	public EnumCollideAction collide(Entity entity) {
		return this.isTangible() && entity.isTangible() ? EnumCollideAction.COLLIDE : EnumCollideAction.PASS_THROUGH;
	}

	public void save(DataOutputStream stream) throws IOException {
		stream.writeInt(getEntityId());
		StreamUtils.writeVector(stream, getPosition());
		StreamUtils.writeArea(stream, getRelativeCollisionBox());
		StreamUtils.writeArea(stream, getRelativeDisplayArea());
		stream.writeBoolean(isTangible());
		stream.writeDouble(getVelocity());
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
	public void draw(Processing core) {
		if (SetupListener.setup) {
			core.strokeWeight(10);
			if (point1 != null) {
				core.stroke(255, 0, 0);
				Vector2f n = CoordinatesUtils.transformIntoProcessingCoordinates(GameCoordinatesUtils.toEngineCoordinates(point1));
				core.point(n.getX(), n.getY());
			}
			if (point2 != null) {
				core.stroke(0, 255, 0);
				Vector2f n = CoordinatesUtils.transformIntoProcessingCoordinates(GameCoordinatesUtils.toEngineCoordinates(point2));
				core.point(n.getX(), n.getY());
			}
			if (point3 != null) {
				core.stroke(0, 0, 255);
				Vector2f n = CoordinatesUtils.transformIntoProcessingCoordinates(GameCoordinatesUtils.toEngineCoordinates(point3));
				core.point(n.getX(), n.getY());
			}
			point1 = point2 = point3 = null;
		}
		super.draw(core);
	}

	@Override
	public void delete() {
		Game.getEntityManager().removeEntity(this);
		super.delete();
	}
}
