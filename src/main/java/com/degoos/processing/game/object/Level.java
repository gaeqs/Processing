package com.degoos.processing.game.object;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.Processing;
import com.degoos.processing.engine.object.Image;
import com.degoos.processing.engine.object.Shape;
import com.degoos.processing.engine.util.CoordinatesUtils;
import com.degoos.processing.engine.util.Validate;
import com.degoos.processing.game.Game;
import com.degoos.processing.game.entity.CollisionBox;
import com.degoos.processing.game.entity.Entity;
import com.degoos.processing.game.entity.SavableEntity;
import com.degoos.processing.game.entity.Teleport;
import com.degoos.processing.game.listener.SetupListener;
import com.degoos.processing.game.util.GameCoordinatesUtils;
import com.degoos.processing.game.util.SavableEntitiesUtils;
import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2f;
import com.flowpowered.math.vector.Vector2i;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

public class Level extends Shape {

	private Vector2d size;
	private Set<SavableEntity> levelEntities;
	private String folder;

	public Level(String folder) {
		super(true, 0, 1, new Vector2d(0, 0));
		Validate.notNull(folder, "Folder cannot be null!");
		this.folder = folder;

		InputStream imageIS = Engine.getResourceInputStream(folder + "/map.png");
		Image image = new Image(imageIS, "png");
		size = new Vector2d(image.getHandled().width / 32D, image.getHandled().height / 32D);

		onTick(0);
		setTexture(image);

		levelEntities = new HashSet<>();
		if (Game.isServer()) {
			InputStream entitiesIS = Engine.getResourceInputStream(folder + "/entities.dat");
			if (entitiesIS != null) {
				SavableEntitiesUtils.loadEntities(this, entitiesIS);
				try {
					entitiesIS.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		Validate.notNull(image, "Image cannot be null!");
	}

	public Vector2d getSize() {
		return size;
	}

	public Set<SavableEntity> getLevelEntities() {
		return levelEntities;
	}

	public void setLevelEntities(Set<SavableEntity> levelEntities) {
		this.levelEntities = levelEntities == null ? new HashSet<>() : levelEntities;
	}

	public Level addEntity(SavableEntity entity) {
		levelEntities.add(entity);
		return this;
	}

	public Level saveLevelEntities() {
		try {
			File folder = new File(this.folder);
			if (!folder.isFile()) folder.delete();
			if (!folder.exists()) folder.mkdirs();
			File file = new File(folder, "entities.dat");
			file.delete();
			file.createNewFile();
			OutputStream outputStream = new FileOutputStream(file);
			SavableEntitiesUtils.saveEntities(this, outputStream);
			outputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this;
	}

	@Override
	public void onTick(long dif) {
		getVertexes().clear();
		Camera camera = Game.getCamera();
		if (size == null) return;
		setOrigin(GameCoordinatesUtils.toEngineCoordinates(new Vector2d()));
		addVertexWithUv(new Vector2d(0, 0), new Vector2i(0, 0));
		addVertexWithUv(new Vector2d(size.getX() / (camera.getXRadius() * 2), 0), new Vector2i(1, 0));
		addVertexWithUv(new Vector2d(size.getX() / (camera.getXRadius() * 2), size.getY() / (camera.getYRadius() * 2)), new Vector2i(1, 1));
		addVertexWithUv(new Vector2d(0, size.getY() / (camera.getYRadius() * 2)), new Vector2i(0, 1));
	}

	@Override
	public void draw(Processing core) {
		super.draw(core);
		if (SetupListener.setup) for (Entity entity : levelEntities) {
			Area area = entity.getCurrentCollisionBox();
			Vector2f minMax = CoordinatesUtils
				.transformIntoProcessingCoordinates(GameCoordinatesUtils.toEngineCoordinates(new Vector2d(area.getMin().getX(), area.getMax().getY())));
			Vector2f maxMin = CoordinatesUtils
				.transformIntoProcessingCoordinates(GameCoordinatesUtils.toEngineCoordinates(new Vector2d(area.getMax().getX(), area.getMin().getY())));
			Vector2f min = CoordinatesUtils.transformIntoProcessingCoordinates(GameCoordinatesUtils.toEngineCoordinates(area.getMin()));
			Vector2f max = CoordinatesUtils.transformIntoProcessingCoordinates(GameCoordinatesUtils.toEngineCoordinates(area.getMax()));
			if (entity instanceof CollisionBox) core.stroke(Color.RED.getRGB());
			else if (entity instanceof Teleport) core.stroke(Color.GREEN.getRGB());
			else core.stroke(Color.GRAY.getRGB());
			core.noFill();
			core.beginShape();
			core.strokeWeight(5);
			core.vertex(min.getX(), min.getY());
			core.vertex(minMax.getX(), minMax.getY());
			core.vertex(max.getX(), max.getY());
			core.vertex(maxMin.getX(), maxMin.getY());
			core.endShape(Processing.CLOSE);
		}
	}
}
