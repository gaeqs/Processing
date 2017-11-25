package com.degoos.processing.game.object;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.Processing;
import com.degoos.processing.engine.object.Image;
import com.degoos.processing.engine.object.Shape;
import com.degoos.processing.engine.util.CoordinatesUtils;
import com.degoos.processing.engine.util.Validate;
import com.degoos.processing.game.Game;
import com.degoos.processing.game.entity.SavableEntity;
import com.degoos.processing.game.listener.SetupListener;
import com.degoos.processing.game.util.CollisionUtils;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Level extends Shape {

	private Vector2d size;
	private List<Area> collisionBoxes;
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

		InputStream collisionBoxesIS = Engine.getResourceInputStream(folder + "/collision.dat");
		if (collisionBoxesIS == null) collisionBoxes = new ArrayList<>();
		else {
			collisionBoxes = CollisionUtils.loadCollisionBoxes(collisionBoxesIS);
			try {
				collisionBoxesIS.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		levelEntities = new HashSet<>();
		InputStream entitiesIS = Engine.getResourceInputStream(folder + "/entities.dat");
		if (entitiesIS != null) {
			SavableEntitiesUtils.loadEntities(this, entitiesIS);
			try {
				entitiesIS.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		Validate.notNull(image, "Image cannot be null!");
	}

	public Vector2d getSize() {
		return size;
	}

	public List<Area> getCollisionBoxes() {
		return collisionBoxes;
	}

	public Level setCollisionBoxes(List<Area> collisionBoxes) {
		this.collisionBoxes = collisionBoxes == null ? new ArrayList<>() : collisionBoxes;
		return this;
	}

	public Level addCollisionBox(Area collisionBox) {
		collisionBoxes.add(collisionBox);
		return this;
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

	public Level saveCollisionBoxes() {
		try {
			File folder = new File(this.folder);
			if (!folder.isFile()) folder.delete();
			if (!folder.exists()) folder.mkdirs();
			File file = new File(folder, "collision.dat");
			file.delete();
			file.createNewFile();
			OutputStream outputStream = new FileOutputStream(file);
			CollisionUtils.saveCollisionBoxes(outputStream, collisionBoxes);
			outputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
		if (SetupListener.setup) for (Area area : collisionBoxes) {
			Vector2f minMax = CoordinatesUtils
				.transformIntoProcessingCoordinates(GameCoordinatesUtils.toEngineCoordinates(new Vector2d(area.getMin().getX(), area.getMax().getY())));
			Vector2f maxMin = CoordinatesUtils
				.transformIntoProcessingCoordinates(GameCoordinatesUtils.toEngineCoordinates(new Vector2d(area.getMax().getX(), area.getMin().getY())));
			Vector2f min = CoordinatesUtils.transformIntoProcessingCoordinates(GameCoordinatesUtils.toEngineCoordinates(area.getMin()));
			Vector2f max = CoordinatesUtils.transformIntoProcessingCoordinates(GameCoordinatesUtils.toEngineCoordinates(area.getMax()));
			core.stroke(Color.RED.getRGB());
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
