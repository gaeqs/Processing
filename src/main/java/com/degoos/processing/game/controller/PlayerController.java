package com.degoos.processing.game.controller;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.Processing;
import com.degoos.processing.engine.event.Listener;
import com.degoos.processing.engine.event.keyboard.KeyPressEvent;
import com.degoos.processing.engine.event.keyboard.KeyReleaseEvent;
import com.degoos.processing.game.Game;
import com.degoos.processing.game.entity.Entity;
import com.sun.glass.events.KeyEvent;

public class PlayerController implements Controller {

	public static boolean up, down, left, right;

	public PlayerController() {
		Engine.getEventManager().registerListener(this);
	}

	@Listener
	public void onKeyPressed(KeyPressEvent event) {
		switch (event.getKeyCode()) {
			case W:
				up = true;
				break;
			case A:
				left = true;
				break;
			case S:
				down = true;
				break;
			case D:
				right = true;
				break;
		}
	}

	@Listener
	public void onKeyReleased(KeyReleaseEvent event) {
		switch (event.getKeyCode()) {
			case W:
				up = false;
				break;
			case A:
				left = false;
				break;
			case S:
				down = false;
				break;
			case D:
				right = false;
				break;
		}
	}

	@Override
	public void onTick(long dif, Entity entity) {
		entity.triggerMove(up, down, left, right, dif);
		Game.getCamera().setPosition(entity.getPosition());
	}
}
