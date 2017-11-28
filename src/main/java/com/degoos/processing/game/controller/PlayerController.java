package com.degoos.processing.game.controller;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.event.Listener;
import com.degoos.processing.engine.event.keyboard.KeyPressEvent;
import com.degoos.processing.engine.event.keyboard.KeyReleaseEvent;
import com.degoos.processing.game.Game;
import com.degoos.processing.game.entity.Entity;

public class PlayerController implements Controller {

	public static boolean up, down, left, right, control;

	public PlayerController() {
		Engine.getEventManager().registerListener(this);
	}

	@Listener
	public void onKeyPressed(KeyPressEvent event) {
		switch (event.getKeyCode()) {
			case UP:
				up = true;
				break;
			case LEFT:
				left = true;
				break;
			case DOWN:
				down = true;
				break;
			case RIGHT:
				right = true;
				break;
			case CONTROL:
				control = true;
				break;
		}
	}

	@Listener
	public void onKeyReleased(KeyReleaseEvent event) {
		switch (event.getKeyCode()) {
			case UP:
				up = false;
				break;
			case LEFT:
				left = false;
				break;
			case DOWN:
				down = false;
				break;
			case RIGHT:
				right = false;
				break;
			case CONTROL:
				control = false;
				break;
		}
	}

	@Override
	public void onTick(long dif, Entity entity) {
		entity.triggerMove(up, down, left, right, dif);
		if (control) entity.setVelocity(0.007D);
		else entity.setVelocity(0.004D);
		Game.getCamera().setPosition(entity.getPosition());
	}
}
