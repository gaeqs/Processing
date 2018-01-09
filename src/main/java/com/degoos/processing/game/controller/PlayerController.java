package com.degoos.processing.game.controller;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.event.Listener;
import com.degoos.processing.engine.event.keyboard.KeyPressEvent;
import com.degoos.processing.engine.event.keyboard.KeyReleaseEvent;
import com.degoos.processing.game.Game;
import com.degoos.processing.game.entity.Entity;
import com.degoos.processing.game.entity.Player;

public class PlayerController implements Controller {

	private boolean up, down, left, right, control, w, a, s, d, q;
	private long lastShoot = System.currentTimeMillis();

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
			case W:
				w = true;
				break;
			case A:
				a = true;
				break;
			case S:
				s = true;
				break;
			case D:
				d = true;
				break;
			case Q:
				q = true;
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
			case W:
				w = false;
				break;
			case A:
				a = false;
				break;
			case S:
				s = false;
				break;
			case D:
				d = false;
				break;
		}
	}

	@Override
	public void onTick(long dif, Entity entity) {
		if (control) entity.setVelocity(0.007D);
		else entity.setVelocity(0.004D);
		entity.triggerMove(up, down, left, right, dif);
		Game.getCamera().setPosition(entity.getPosition());

		if (!Game.isServer()  || !(entity instanceof Player)) return;

		if ((w || a || s || d) && lastShoot + 200 < System.currentTimeMillis()) {
			((Player) entity).shootAuraSphere(w, s, a, d);
			lastShoot = System.currentTimeMillis();
		}
		if (q) {
			q = false;
			((Player) entity).activateShadowSpecial();
		}
	}
}
