package com.degoos.processing.game;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.enums.EnumTextureSampling;
import com.degoos.processing.engine.object.Text;
import com.degoos.processing.game.controller.PlayerController;
import com.degoos.processing.game.entity.Player;
import com.degoos.processing.game.listener.SetupListener;
import com.degoos.processing.game.manager.EntityManager;
import com.degoos.processing.game.object.Camera;
import com.degoos.processing.game.object.Level;
import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2i;
import java.awt.Color;

public class Game {

	private static Camera camera;
	private static Level map;
	private static Player player;
	private static EntityManager entityManager;

	public static void main(String[] args) {
		Engine.startEngine(new Vector2i(1280, 720));
		Engine.setTextureSampling(EnumTextureSampling.NEAREST);
		double ref = 1280D / 720D;

		entityManager = new EntityManager();
		camera = new Camera(new Vector2d(12, 10), ref * 4, 4);
		map = new Level("map");

		player = new Player(new Vector2d(12, 8), new PlayerController());

		Engine.getEventManager().registerListener(new SetupListener());

		new Text(true, 0, 0, "", new Vector2d(0, 0.98), Color.BLACK, 20) {
			@Override
			public void onTick(long dif) {
				setText(String.valueOf(dif));
			}
		};

		new Text(true, 0, 0, "", new Vector2d(0, 0.02), Color.MAGENTA, 20) {
			@Override
			public void onTick(long dif) {
				if (SetupListener.setup) {
					setVisible(true);
					setText("Setup Mode : " + SetupListener.setupMode);
				} else setVisible(false);
			}
		};
	}

	public static Camera getCamera() {
		return camera;
	}

	public static Level getMap() {
		return map;
	}

	public static Player getPlayer() {
		return player;
	}

	public static EntityManager getEntityManager() {
		return entityManager;
	}
}
