package com.degoos.processing.game;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.object.Text;
import com.degoos.processing.game.listener.ScreenListener;
import com.degoos.processing.game.listener.SetupListener;
import com.degoos.processing.game.object.Camera;
import com.degoos.processing.game.object.Level;
import com.flowpowered.math.vector.Vector2d;
import java.awt.Color;

public class MapLoader {

	public static void load() {
		double ref = (double) Engine.getCore().width / (double) Engine.getCore().height;

		Game.camera = new Camera(new Vector2d(), ref * 4, 4);
		Game.level = new Level("map");

		Engine.getEventManager().registerListener(new SetupListener());
		Engine.getEventManager().registerListener(new ScreenListener());

		new Text(true, 0, 0, "", new Vector2d(0, 0.98), Color.BLACK, 20) {
			@Override
			public void onTick(long dif) {
				setText(String.valueOf(dif) + (SetupListener.setup ? Game.player.getPosition() : ""));
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
		Game.getEntityManager().getEntities().forEach(entity -> entity.setPosition(entity.getPosition()));
	}

}
