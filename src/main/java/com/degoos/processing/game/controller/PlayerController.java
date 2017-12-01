package com.degoos.processing.game.controller;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.event.Listener;
import com.degoos.processing.engine.event.keyboard.KeyPressEvent;
import com.degoos.processing.engine.event.keyboard.KeyReleaseEvent;
import com.degoos.processing.game.Game;
import com.degoos.processing.game.entity.Entity;
import com.degoos.processing.game.entity.Player;

public class PlayerController implements Controller {

	private boolean up, down, left, right, control, x;
	private boolean shot;

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
			case X:
				x = true;
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
			case X:
				x = false;
				shot = false;
				break;
		}
	}

	@Override
	public void onTick(long dif, Entity entity) {
		if (control) entity.setVelocity(0.007D);
		else entity.setVelocity(0.004D);
		entity.triggerMove(up, down, left, right, dif);
		Game.getCamera().setPosition(entity.getPosition());

		if (Game.isServer() && x && !shot && entity instanceof Player) {
			((Player) entity).shootAuraSphere();
			shot = true;
		}
	}
}
