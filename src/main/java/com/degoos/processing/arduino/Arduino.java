package com.degoos.processing.arduino;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.Processing;
import com.degoos.processing.engine.enums.EnumTextureSampling;
import com.degoos.processing.engine.event.Listener;
import com.degoos.processing.engine.event.draw.AfterDrawEvent;
import com.degoos.processing.engine.util.Serial;
import com.flowpowered.math.vector.Vector2i;
import java.awt.Color;

public class Arduino {

	private static Serial serial;
	private static int color;

	public static void main(String[] args) {
		Engine.startEngine(new Vector2i(1280, 720));
		Engine.setTextureSampling(EnumTextureSampling.NEAREST);
		serial = new Serial(Processing.getInstance());

		Engine.getCore().setBackground(new Color(color, 0, 0));
		Engine.getEventManager().registerListener(new Arduino());
	}

	@Listener
	public void onTick(AfterDrawEvent event) {
		while (serial.available() > 0) {
			color = serial.read();
		}
		Engine.getCore().setBackground(new Color(color, 0, 0));
	}
}
