package com.degoos.processing.test;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.enums.EnumTextureSampling;
import com.degoos.processing.engine.object.Animation;
import com.degoos.processing.engine.object.Shape;
import com.degoos.processing.engine.object.Text;
import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2i;
import java.awt.Color;

public class Test {

	public static Text text;

	public static void main(String[] args) {
		Engine.startEngine(new Vector2i(1280, 720));
		Engine.getCore().setBackground(Color.CYAN);
		Engine.setTextureSampling(EnumTextureSampling.NEAREST);
		Shape shape = new Shape(true, 0, 0, new Vector2d(0.5, 0.8));
		shape.addVertexWithUv(new Vector2d(40.1 / Engine.getCore().width, 40.1 / Engine.getCore().height), new Vector2i(1, 1));
		shape.addVertexWithUv(new Vector2d(40.1 / Engine.getCore().width, -40.1 / Engine.getCore().height), new Vector2i(1, 0));
		shape.addVertexWithUv(new Vector2d(-40.1 / Engine.getCore().width, -40.1 / Engine.getCore().height), new Vector2i(0, 0));
		shape.addVertexWithUv(new Vector2d(-40.1 / Engine.getCore().width, 40.1 / Engine.getCore().height), new Vector2i(0, 1));
		shape.setTexture(new Animation("riolu/walk/up", "png"));
		text = new Text(true, 0, 0, "OMAE WA MOU SHINDEIRU", new Vector2d(0.4, 0.4), Color.MAGENTA, 80) {
			@Override
			public void onTick(long dif) {
				setPosition(getPosition().add(0.01F, 0));
				if (getPosition().getX() > 1.2) setPosition(getPosition().sub(3, 0));
			}
		};

		new Text(true, 0, 0, "", new Vector2d(0, 0.98), 20) {
			@Override
			public void onTick(long dif) {
				setText(String.valueOf(dif));
			}
		};

		Engine.getEventManager().registerListener(new TestListener());
	}

}
