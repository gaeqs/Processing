package com.degoos.processing.game.entity;

import com.degoos.processing.engine.Processing;
import com.degoos.processing.engine.object.Animation;
import com.degoos.processing.engine.object.Image;
import com.degoos.processing.engine.util.Validate;
import com.degoos.processing.game.controller.Controller;
import com.degoos.processing.game.enums.EnumFacingDirection;
import com.degoos.processing.game.object.Area;
import com.flowpowered.math.vector.Vector2d;
import java.util.HashMap;
import java.util.Map;

public class Player extends Entity {

	private EnumFacingDirection direction;
	private Map<EnumFacingDirection, Image> standAnimations;
	private Map<EnumFacingDirection, Image> walkingAnimations;
	private boolean walking;

	public Player(Vector2d position, Controller controller) {
		super(position, new Area(new Vector2d(-0.6, 0), new Vector2d(0.6, 0.5)), new Area(new Vector2d(-0.7, 0), new Vector2d(0.7, 1.3)), true, 0.004D, controller);
		setDrawPriority(2);
		setTickPriority(0);

		standAnimations = new HashMap<>();
		walkingAnimations = new HashMap<>();
		standAnimations.put(EnumFacingDirection.DOWN, new Animation("riolu/stand/down", "png"));
		standAnimations.put(EnumFacingDirection.UP, new Animation("riolu/stand/up", "png"));
		standAnimations.put(EnumFacingDirection.RIGHT, new Animation("riolu/stand/right", "png"));
		standAnimations.put(EnumFacingDirection.LEFT, new Animation("riolu/stand/left", "png"));

		walkingAnimations.put(EnumFacingDirection.DOWN, new Animation("riolu/walk/down", "png"));
		walkingAnimations.put(EnumFacingDirection.UP, new Animation("riolu/walk/up", "png"));
		walkingAnimations.put(EnumFacingDirection.RIGHT, new Animation("riolu/walk/right", "png"));
		walkingAnimations.put(EnumFacingDirection.LEFT, new Animation("riolu/walk/left", "png"));

		this.walking = false;
		this.direction = EnumFacingDirection.DOWN;
		setTexture(getAnimation(EnumFacingDirection.DOWN));
	}

	public EnumFacingDirection getDirection() {
		return direction;
	}

	public Player setDirection(EnumFacingDirection direction) {
		Validate.notNull(direction, "Direction cannot be null!");
		this.direction = direction;
		refreshAnimation();
		return this;
	}

	public Player setDirection(EnumFacingDirection direction, boolean toggleWalking) {
		Validate.notNull(direction, "Direction cannot be null!");
		if (this.direction == direction && !toggleWalking) return this;
		this.direction = direction;
		refreshAnimation();
		return this;
	}

	public Map<EnumFacingDirection, Image> getStandAnimations() {
		return standAnimations;
	}

	public Map<EnumFacingDirection, Image> getWalkingAnimations() {
		return walkingAnimations;
	}

	public void refreshAnimation() {
		Image image = getAnimation(direction);
		if (image instanceof Animation) ((Animation) image).reset();
		setTexture(image);
	}

	public Image getAnimation(EnumFacingDirection direction) {
		if (walking) {
			if (walkingAnimations.containsKey(direction)) return walkingAnimations.get(direction);
		} else if (standAnimations.containsKey(direction)) return standAnimations.get(direction);
		return getTexture();
	}

	@Override
	public void triggerMove(boolean up, boolean down, boolean left, boolean right, long dif) {
		double vel = getVelocity() * dif;
		if (left && right) left = right = false;
		if (up && down) up = down = false;
		boolean wasWalking = walking;
		walking = down || up || left || right;
		if (wasWalking && !walking) refreshAnimation();

		Vector2d move = new Vector2d();
		if (left) {
			move = move.add(-vel, 0);
			if (!up && !down) setDirection(EnumFacingDirection.LEFT, wasWalking != walking);
		}
		if (right) {
			move = move.add(vel, 0);
			if (!up && !down) setDirection(EnumFacingDirection.RIGHT, wasWalking != walking);
		}
		if (up) {
			move = move.add(0, vel);
			setDirection(EnumFacingDirection.UP, wasWalking != walking);
		}
		if (down) {
			move = move.add(0, -vel);
			setDirection(EnumFacingDirection.DOWN, wasWalking != walking);
		}
		if(move.getX() == 0 && move.getY() == 0) return;
		move(move);
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
