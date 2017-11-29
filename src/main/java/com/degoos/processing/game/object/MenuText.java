package com.degoos.processing.game.object;

import com.degoos.processing.engine.Engine;
import com.degoos.processing.engine.enums.EnumKeyboardKey;
import com.degoos.processing.engine.event.Listener;
import com.degoos.processing.engine.event.keyboard.KeyPressEvent;
import com.degoos.processing.engine.object.Shape;
import com.degoos.processing.engine.object.Text;
import com.degoos.processing.game.Game;
import com.flowpowered.math.vector.Vector2d;
import java.awt.Color;

public class MenuText extends Text {

	private Shape background;
	private boolean ipText, selected;
	private MenuText other;

	public MenuText(Shape background, boolean ipText, MenuText other) {
		super(true, 0, 0, "", new Vector2d(0.0771875, ipText ? 0.63366 : 0.83366), ipText ? Color.PINK.darker() : Color.PINK, 40, Game.getFont());
		Engine.getEventManager().registerListener(this);
		this.background = background;
		this.ipText = ipText;
		this.other = other;
		this.selected = !ipText;
	}

	public void setOther(MenuText other) {
		this.other = other;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Listener
	public void onKeyPress(KeyPressEvent event) {
		if (!selected) return;
		if ((ipText && event.getKeyCode() == EnumKeyboardKey.UP) || (!ipText && event.getKeyCode() == EnumKeyboardKey.DOWN)) {
			selected = false;
			other.setSelected(true);
			setColor(Color.PINK.darker());
			other.setColor(Color.PINK);
			return;
		}
		if (event.getKeyCode() == EnumKeyboardKey.MODIFIER_BUTTON_PRIMARY) return;
		if (event.getKeyCode() == EnumKeyboardKey.ENTER) {
			if (getText().isEmpty() || other.getText().isEmpty()) return;
			delete();
			background.delete();
			other.delete();

			String nick = ipText ? other.getText() : getText();
			String ip = ipText ? getText() : other.getText();
			Game.load(nick.replace("\uFFFF", ""), ip.replace("\uFFFF", ""));
			return;
		}
		if (event.getKeyId() == 8) {
			if (!getText().isEmpty()) setText(getText().substring(0, getText().length() - 1));
		} else {
			if (getText().length() < (ipText ? 22 : 16)) setText(getText() + event.getKey());
		}
	}

	@Override
	public void delete() {
		Engine.getEventManager().unregisterListener(this);
		super.delete();
	}
}
