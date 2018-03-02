package com.degoos.processing.arduino;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.Processing;
import com.degoos.processing.engine.enums.EnumTextureSampling;
import com.degoos.processing.engine.event.Listener;
import com.degoos.processing.engine.event.draw.AfterDrawEvent;
import com.degoos.processing.engine.util.Serial;
import com.flowpowered.math.vector.Vector2i;
import java.awt.Color;
import java.util.Arrays;

public class Arduino {

	private static Serial serial;
	private static int color;

	public static void main(String[] args) {
		Engine.startEngine(new Vector2i(1280, 720));
		Engine.setTextureSampling(EnumTextureSampling.NEAREST);
		System.out.println(Arrays.toString(Serial.list()));
		serial = new Serial(Processing.getInstance(), Serial.list()[0]);

		Engine.getCore().setBackground(new Color(color, 0, 0));
		Engine.getEventManager().registerListener(new Arduino());
	}

	@Listener
	public void onTick(AfterDrawEvent event) {
		while (serial.available() > 0) {
			color = Math.min(255, Math.max(0, serial.read()));
		}
		System.out.println(color);
		Engine.getCore().setBackground(new Color(color, 0, 0));
	}
}
