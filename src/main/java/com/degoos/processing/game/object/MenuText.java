package com.degoos.processing.game.object;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.enums.EnumKeyboardKey;
import com.degoos.processing.engine.event.Listener;
import com.degoos.processing.engine.event.keyboard.KeyPressEvent;
import com.degoos.processing.engine.object.Shape;
import com.degoos.processing.engine.object.Text;
import com.degoos.processing.game.Game;
import com.degoos.processing.game.MapLoader;
import com.flowpowered.math.vector.Vector2d;
import java.awt.Color;

public class MenuText extends Text {

	private Shape background;

	public MenuText(Shape background) {
		super(true, 0, 0, "", new Vector2d(0.0771875, 0.83366), Color.PINK, 40, Game.getFont());
		Engine.getEventManager().registerListener(this);
		this.background = background;
	}


	@Listener
	public void onKeyPress(KeyPressEvent event) {
		if (event.getKeyCode() == EnumKeyboardKey.MODIFIER_BUTTON_PRIMARY) return;
		if (event.getKeyCode() == EnumKeyboardKey.ENTER) {
			if (getText().isEmpty()) return;
			delete();
			background.delete();
			MapLoader.load();
			return;
		}
		if (event.getKeyId() == 8) {
			if (!getText().isEmpty()) setText(getText().substring(0, getText().length() - 1));
		} else {
			if (getText().length() < 16) setText(getText() + event.getKey());
		}
	}

	@Override
	public void delete() {
		Engine.getEventManager().unregisterListener(this);
		super.delete();
	}
}
