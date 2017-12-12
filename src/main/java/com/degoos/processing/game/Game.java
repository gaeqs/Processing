package com.degoos.processing.game;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.enums.EnumTextureSampling;
import com.degoos.processing.engine.object.Font;
import com.degoos.processing.engine.object.Image;
import com.degoos.processing.engine.object.Shape;
import com.degoos.processing.game.controller.PlayerController;
import com.degoos.processing.game.entity.Player;
import com.degoos.processing.game.manager.EntityManager;
import com.degoos.processing.game.network.GameServer;
import com.degoos.processing.game.network.ServerConnection;
import com.degoos.processing.game.object.Camera;
import com.degoos.processing.game.object.Level;
import com.degoos.processing.game.object.MenuText;
import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2i;
import java.awt.Color;
import java.util.Arrays;

public class Game {

	protected static Camera camera;
	protected static Level level;
	protected static Player player;
	protected static EntityManager entityManager;
	private static GameServer gameServer;
	private static ServerConnection serverConnection;
	private static Font font;
	private static boolean isServer;
	private static Shape loadingShape, loadingShapeText;
	private static MenuText menu;

	public static void main(String[] args) {
		Game.entityManager = new EntityManager();
		Engine.startEngine(new Vector2i(1280, 720));
		Engine.setTextureSampling(EnumTextureSampling.NEAREST);

		font = new Font(Engine.getResourceInputStream("font/font.vlw"));

		Shape shape = new Shape(new Vector2d()).setTexture(new Image(Engine.getResourceInputStream("background/background.png"), "png"));
		shape.addVertexWithUv(new Vector2d(), new Vector2i());
		shape.addVertexWithUv(new Vector2d(0, 1), new Vector2i(0, 1));
		shape.addVertexWithUv(new Vector2d(1, 1), new Vector2i(1, 1));
		shape.addVertexWithUv(new Vector2d(1, 0), new Vector2i(1, 0));
		shape.setVisible(true);

		menu = new MenuText(shape, false, null);
		menu.setOther(new MenuText(shape, true, menu));

		loadingShape = new Shape(false, 100, 0, new Vector2d(), Arrays.asList(new Vector2d(), new Vector2d(0, 1), new Vector2d(1, 1), new Vector2d(1, 0)))
			.setFullColor(Color.BLACK);

		loadingShapeText = new Shape(false, 100, 0, new Vector2d(0.5, 0));
		loadingShapeText.addVertexWithUv(new Vector2d(), new Vector2i());
		loadingShapeText.addVertexWithUv(new Vector2d(0, 0.3), new Vector2i(0, 1));
		loadingShapeText.addVertexWithUv(new Vector2d(0.5, 0.3), new Vector2i(1, 1));
		loadingShapeText.addVertexWithUv(new Vector2d(0.5, 0), new Vector2i(1, 0));
		loadingShapeText.setTexture(new Image(Engine.getResourceInputStream("gui/loading.png"), "png"));

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if (serverConnection != null) serverConnection.disconnect();
			if (gameServer != null) gameServer.disconnect();
		}));
	}

	public static boolean load(String nick, String ip) {
		setLoading(true);
		if (ip.equalsIgnoreCase("local")) {
			isServer = true;
			gameServer = new GameServer();
			MapLoader.load();
			setPlayer(new Player(new Vector2d(12, 8), new PlayerController(), nick));
			setLoading(false);
			return true;
		} else {
			isServer = false;
			return (serverConnection = new ServerConnection(nick, ip)).isLoaded();
		}
	}

	public static Camera getCamera() {
		return camera;
	}

	public static Level getLevel() {
		return level;
	}

	public static Player getPlayer() {
		return player;
	}

	public static void setPlayer(Player player) {
		Game.player = player;
		camera.setPosition(player.getPosition());
	}

	public static EntityManager getEntityManager() {
		return entityManager;
	}

	public static boolean isServer() {
		return isServer;
	}

	public static GameServer getGameServer() {
		return gameServer;
	}

	public static ServerConnection getServerConnection() {
		return serverConnection;
	}

	public static void refreshCameraRadius(Vector2i size) {
		double ref = Math.max(Math.min((double) size.getX() / (double) size.getY(), 2), 0.8);
		camera = new Camera(new Vector2d(12, 8), ref * 4, 4);
	}

	public static void setLoading(boolean loading) {
		loadingShape.setVisible(loading);
		loadingShapeText.setVisible(loading);
	}

	public static boolean isLoading() {
		return loadingShape.isVisible();
	}

	public static MenuText getMenu() {
		return menu;
	}

	public static Font getFont() {
		return font;
	}
}
