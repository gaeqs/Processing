package com.degoos.processing.game.entity;

import com.degoos.processing.engine.Processing;
import com.degoos.processing.engine.object.Arc;
import com.degoos.processing.game.Game;
import com.degoos.processing.game.controller.Controller;
import com.degoos.processing.game.object.Area;
import com.degoos.processing.game.util.GameCoordinatesUtils;
import com.flowpowered.math.vector.Vector2d;
import java.awt.Color;

public class LivingEntity extends Entity {

	private double health, maxHealth;
	private Arc healthBar, steticArc;

	public LivingEntity(Vector2d position, Area relativeCollisionBox, Area relativeDisplayArea, boolean tangible, double velocity, boolean canMove, Controller
		controller,
		double health, double maxHealth) {
		this(Game.getEntityManager()
			.getFirstAvailableId(), position, relativeCollisionBox, relativeDisplayArea, tangible, velocity, canMove, controller, health, maxHealth);
	}

	public LivingEntity(int id, Vector2d position, Area relativeCollisionBox, Area relativeDisplayArea, boolean tangible, double velocity, boolean canMove,
		Controller controller, double health, double maxHealth) {
		super(id, position, relativeCollisionBox, relativeDisplayArea, tangible, velocity, canMove, controller);
		this.health = health;
		this.maxHealth = maxHealth;
		healthBar = new Arc(new Vector2d(), new Vector2d(), 0, 0).setLineColor(Color.BLACK).setLineSize(1.5F);
		steticArc = new Arc(false, 2, 0, new Vector2d(), new Vector2d(), 0, Processing.TAU).setFullColor(Color.BLACK);
		recalculateHealthBar();
	}

	public double getHealth() {
		return health;
	}

	public void setHealth(double health) {
		this.health = Math.max(Math.min(health, maxHealth), 0);
		recalculateHealthBar();
	}

	public double getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(double maxHealth) {
		this.maxHealth = Math.max(maxHealth, 0);
		if (health > maxHealth) health = maxHealth;
		recalculateHealthBar();
	}

	private void recalculateHealthBar() {
		if (health >= maxHealth) {
			healthBar.setVisible(false);
			steticArc.setVisible(false);
		} else {
			healthBar.setVisible(true);
			steticArc.setVisible(true);
			healthBar.setMin(GameCoordinatesUtils.toEngineCoordinates(getCurrentDisplayArea().getMax().add(0, 0)));
			healthBar.setMax(GameCoordinatesUtils.toEngineCoordinates(getCurrentDisplayArea().getMax().add(0.3, 0.3)));
			steticArc.setMin(GameCoordinatesUtils.toEngineCoordinates(getCurrentDisplayArea().getMax().add(0.1, 0.1)));
			steticArc.setMax(GameCoordinatesUtils.toEngineCoordinates(getCurrentDisplayArea().getMax().add(0.2, 0.2)));
			float healthScale = (float) getHealth() / (float) getMaxHealth();
			healthBar.setStopAngle(healthScale * Processing.TAU);
			healthBar.setFillColor(new Color(1 - healthScale, healthScale, 0));
		}
	}

	@Override
	public void draw(Processing core) {
		super.draw(core);
	}
}
