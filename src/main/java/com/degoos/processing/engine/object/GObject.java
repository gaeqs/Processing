package com.degoos.processing.engine.object;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.Processing;

public abstract class GObject {

	private int id;
	private boolean visible;
	private double drawPriority, tickPriority;

	public GObject() {
		this(false, 0, 0);
	}

	public GObject(boolean visible, double drawPriority, double tickPriority) {
		this.id = Engine.getObjectManager().addGObject(this);
		this.visible = visible;
		this.drawPriority = drawPriority;
		this.tickPriority = tickPriority;
	}

	public int getId() {
		return id;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public double getDrawPriority() {
		return drawPriority;
	}

	public void setDrawPriority(double drawPriority) {
		this.drawPriority = drawPriority;
	}

	public double getTickPriority() {
		return tickPriority;
	}

	public void setTickPriority(double tickPriority) {
		this.tickPriority = tickPriority;
	}

	public void delete() {
		Engine.getObjectManager().removeObject(id);
	}

	public abstract void draw(Processing core);

	public abstract void onTick(long dif);

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GObject gObject = (GObject) o;

		return id == gObject.id;
	}

	@Override
	public int hashCode() {
		return id;
	}
}
