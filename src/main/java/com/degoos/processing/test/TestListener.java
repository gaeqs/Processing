package com.degoos.processing.test;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.enums.EnumTextureSampling;
import com.degoos.processing.engine.event.Listener;
import com.degoos.processing.engine.event.keyboard.KeyPressEvent;
import com.degoos.processing.engine.event.mouse.MousePressEvent;
import com.degoos.processing.engine.event.mouse.MouseReleaseEvent;
import com.degoos.processing.engine.object.Line;
import com.degoos.processing.test.Test;
import com.flowpowered.math.vector.Vector2d;
import java.awt.Color;

public class TestListener {

	private Vector2d lastPoint;
	private Line line;

	@Listener
	public void onMouseDrag(MousePressEvent event) {
		if (lastPoint == null) {
			lastPoint = event.getPosition();
			return;
		}
		line = new Line(true, -1, -1, lastPoint, event.getPosition(), Color.YELLOW, 5) {
			@Override
			public void onTick(long dif) {
				if (equals(line)) setEnd(Engine.getMousePosition());
			}
		};
	}

	@Listener
	public void onMouseRelease(MouseReleaseEvent event) {
		line = null;
		lastPoint = event.getPosition();
	}

	@Listener
	public void onKeyClicked(KeyPressEvent event) {
		String text = Test.text.getText();
		if (event.getKeyCode().getId() == 8 && !text.isEmpty()) Test.text.setText(text.substring(0, text.length() - 1));
		else Test.text.setText(text + event.getKey());
		System.out.println(event.getKeyCode());
		if (event.getKeyCode().getId() == 38) {
			Engine.setTextureSampling(EnumTextureSampling.NEAREST);
		}
		if (event.getKeyCode().getId() == 40) {
			Engine.setTextureSampling(EnumTextureSampling.BILINEAR);
		}
		if (event.getKeyCode().getId() == 37) {
			Engine.setTextureSampling(EnumTextureSampling.LINEAR);
		}
		if (event.getKeyCode().getId() == 39) {
			Engine.setTextureSampling(EnumTextureSampling.TRILINEAR);
		}
	}
}

