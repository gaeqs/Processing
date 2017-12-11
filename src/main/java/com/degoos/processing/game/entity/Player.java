package com.degoos.processing.game.entity;

import com.degoos.processing.engine.Processing;
import com.degoos.processing.engine.object.Animation;
import com.degoos.processing.engine.object.Image;
import com.degoos.processing.engine.util.Validate;
import com.degoos.processing.game.Game;
import com.degoos.processing.game.controller.Controller;
import com.degoos.processing.game.enums.EnumCollideAction;
import com.degoos.processing.game.enums.EnumFacingDirection;
import com.degoos.processing.game.network.packet.Packet;
import com.degoos.processing.game.network.packet.out.PacketOutPlayerChangeAnimation;
import com.degoos.processing.game.object.Area;
import com.flowpowered.math.vector.Vector2d;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Player extends LivingEntity {

	private EnumFacingDirection direction;
	private Map<EnumFacingDirection, Image> standAnimations;
	private Map<EnumFacingDirection, Image> walkingAnimations;
	private boolean walking;

	public Player(Vector2d position, Controller controller) {
		this(-1, position, controller);
	}


	public Player(int id, Vector2d position, Controller controller) {
		super(id, position, new Area(new Vector2d(-0.6, 0), new Vector2d(0.6, 0.5)), new Area(new Vector2d(-0.7, 0), new Vector2d(0.7, 1.3)), true, 0.004D, true,
			controller, 100, 100);
		loadInstance();
	}

	public Player(DataInputStream inputStream) throws IOException {
		super(inputStream);
		loadInstance();
	}

	public Player(DataInputStream inputStream, Controller controller) throws IOException {
		super(inputStream, controller);
		loadInstance();
	}

	private void loadInstance() {
		setDrawPriority(2);
		setTickPriority(0);

		this.walking = false;
		this.direction = EnumFacingDirection.DOWN;

		standAnimations = new HashMap<>();
		walkingAnimations = new HashMap<>();
		standAnimations.put(EnumFacingDirection.DOWN, new Animation("riolu/stand/down", "png"));
		standAnimations.put(EnumFacingDirection.UP, new Animation("riolu/stand/up", "png"));
		standAnimations.put(EnumFacingDirection.RIGHT, new Animation("riolu/stand/right", "png"));
		standAnimations.put(EnumFacingDirection.LEFT, new Animation("riolu/stand/left", "png"));
		standAnimations.put(EnumFacingDirection.DOWN_LEFT, standAnimations.get(EnumFacingDirection.DOWN));
		standAnimations.put(EnumFacingDirection.DOWN_RIGHT, standAnimations.get(EnumFacingDirection.DOWN));
		standAnimations.put(EnumFacingDirection.UP_LEFT, standAnimations.get(EnumFacingDirection.UP));
		standAnimations.put(EnumFacingDirection.UP_RIGHT, standAnimations.get(EnumFacingDirection.UP));

		walkingAnimations.put(EnumFacingDirection.DOWN, new Animation("riolu/walk/down", "png"));
		walkingAnimations.put(EnumFacingDirection.UP, new Animation("riolu/walk/up", "png"));
		walkingAnimations.put(EnumFacingDirection.RIGHT, new Animation("riolu/walk/right", "png"));
		walkingAnimations.put(EnumFacingDirection.LEFT, new Animation("riolu/walk/left", "png"));
		walkingAnimations.put(EnumFacingDirection.DOWN_LEFT, walkingAnimations.get(EnumFacingDirection.DOWN));
		walkingAnimations.put(EnumFacingDirection.DOWN_RIGHT, walkingAnimations.get(EnumFacingDirection.DOWN));
		walkingAnimations.put(EnumFacingDirection.UP_LEFT, walkingAnimations.get(EnumFacingDirection.UP));
		walkingAnimations.put(EnumFacingDirection.UP_RIGHT, walkingAnimations.get(EnumFacingDirection.UP));
		setTexture(getAnimation(EnumFacingDirection.DOWN));
	}

	public EnumFacingDirection getDirection() {
		return direction;
	}

	public Player setDirection(EnumFacingDirection direction) {
		Validate.notNull(direction, "Direction cannot be null!");
		this.direction = direction;
		refreshAnimation();

		if (Game.isServer()) {
			Packet packet = new PacketOutPlayerChangeAnimation(getEntityId(), direction, walking);
			Game.getGameServer().getServerClients().stream().filter(client -> !client.getPlayer().equals(this)).forEach(client -> client.sendPacket(packet));
		}
		return this;
	}

	public Player setDirection(EnumFacingDirection direction, boolean toggleWalking) {
		Validate.notNull(direction, "Direction cannot be null!");
		if (this.direction == direction && !toggleWalking) return this;
		return setDirection(direction);
	}

	public Map<EnumFacingDirection, Image> getStandAnimations() {
		return standAnimations;
	}

	public Map<EnumFacingDirection, Image> getWalkingAnimations() {
		return walkingAnimations;
	}

	public void refreshAnimation() {
		Image image = getAnimation(direction);
		if (image.equals(getTexture())) return;
		if (image instanceof Animation) ((Animation) image).reset();
		setTexture(image);
	}

	public Image getAnimation(EnumFacingDirection direction) {
		if (walking) {
			if (walkingAnimations.containsKey(direction)) return walkingAnimations.get(direction);
		} else if (standAnimations.containsKey(direction)) return standAnimations.get(direction);
		return getTexture();
	}

	public boolean isWalking() {
		return walking;
	}

	public void setWalking(boolean walking) {
		this.walking = walking;
	}

	public void shootAuraSphere(boolean up, boolean down, boolean left, boolean right) {
		if (left && right) left = right = false;
		if (up && down) up = down = false;
		if (!(up || down || left || right)) return;
		Vector2d position = getPosition().add(0, 0.3);
		EnumFacingDirection shootDirection = EnumFacingDirection.getFacingDirection(up, down, left, right);
		Vector2d direction = shootDirection.getNormalVector().mul(shootDirection.isDiagonal() ? Math.cos(45) : 1);
		AuraSphere auraSphere = new AuraSphere(position.add(!shootDirection.isDiagonal() ? direction.mul(0.5) : direction), null, 1, direction, getEntityId());
		auraSphere.sendSpawnPacket();
	}

	@Override
	public void triggerMove(boolean up, boolean down, boolean left, boolean right, long dif) {
		if (!canMove()) {
			if (walking) {
				walking = false;
				refreshAnimation();
			}
			return;
		}
		double vel = getVelocity() * dif;
		if (left && right) left = right = false;
		if (up && down) up = down = false;
		boolean wasWalking = walking;
		walking = down || up || left || right;
		if (wasWalking && !walking) setDirection(getDirection());
		if (!(up || down || left || right)) return;

		EnumFacingDirection facingDirection = EnumFacingDirection.getFacingDirection(up, down, left, right);
		Vector2d velocity = facingDirection.getNormalVector().mul(vel);
		move(velocity);
		setDirection(facingDirection, wasWalking != walking);
	}

	@Override
	public EnumCollideAction collide(Entity entity) {
		if (entity instanceof Player) return EnumCollideAction.PASS_THROUGH;
		return super.collide(entity);
	}

	@Override
	public void onTick(long dif) {
		super.onTick(dif);
	}


	@Override
	public void draw(Processing core) {
		super.draw(core);
	}
}
