package com.degoos.processing.game.listener;

import com.degoos.processing.engine.enums.EnumKeyboardKey;
import com.degoos.processing.engine.enums.EnumMouseKey;
import com.degoos.processing.engine.event.Listener;
import com.degoos.processing.engine.event.keyboard.KeyPressEvent;
import com.degoos.processing.engine.event.mouse.MousePressEvent;
import com.degoos.processing.game.Game;
import com.degoos.processing.game.entity.Teleport;
import com.degoos.processing.game.object.Area;
import com.degoos.processing.game.object.Camera;
import com.flowpowered.math.vector.Vector2d;
import java.util.ArrayList;

public class SetupListener {

	private EnumKeyboardKey[] keys = {EnumKeyboardKey.UP, EnumKeyboardKey.UP, EnumKeyboardKey.DOWN, EnumKeyboardKey.DOWN, EnumKeyboardKey.LEFT, EnumKeyboardKey.RIGHT,
		EnumKeyboardKey.LEFT, EnumKeyboardKey.RIGHT, EnumKeyboardKey.B, EnumKeyboardKey.A};
	private int current = 0;
	public static boolean setup = false;
	public static int setupMode = 0;

	@Listener
	public void onKeyPress(KeyPressEvent event) {
		if (setup) {
			if (event.getKeyCode().getId() == 38) {
				setup = false;
				Game.getPlayer().setTangible(true);
			}
			if (event.getKeyCode() == EnumKeyboardKey.LEFT) setupMode--;
			else if (event.getKeyCode() == EnumKeyboardKey.RIGHT) setupMode++;
		} else {
			EnumKeyboardKey key = keys[current];
			if (event.getKeyCode() == key) {
				current++;
				if (keys.length <= current) {
					current = 0;
					setup = true;
					Game.getPlayer().setTangible(false);
				}
			} else current = 0;
		}
	}

	private Vector2d collisionFirstPos = null;

	@Listener
	public void collisionSetup(MousePressEvent event) {
		if (!setup || setupMode != 0) return;
		Camera camera = Game.getCamera();
		Vector2d pos = event.getPosition().mul(camera.getXRadius() * 2, camera.getYRadius() * 2).add(camera.getPosition()).sub(camera.getXRadius(), camera.getYRadius());
		if (event.getPressedKey() == EnumMouseKey.PRIMARY) {
			if (collisionFirstPos == null) collisionFirstPos = pos;
			else {
				Game.getMap().addCollisionBox(new Area(collisionFirstPos, pos));
				Game.getMap().saveCollisionBoxes();
				collisionFirstPos = null;
			}
		}
		if (event.getPressedKey() == EnumMouseKey.TERTIARY) {
			new ArrayList<>(Game.getMap().getCollisionBoxes()).stream().filter(area -> area.isInside(pos))
				.forEach(area -> Game.getMap().getCollisionBoxes().remove(area));
			Game.getMap().saveCollisionBoxes();
		}
	}

	private Vector2d tpFirstPos = null;
	private Vector2d tpSecondPos = null;

	@Listener
	public void tpSetup(MousePressEvent event) {
		if (!setup || setupMode != 1) return;
		Camera camera = Game.getCamera();
		Vector2d pos = event.getPosition().mul(camera.getXRadius() * 2, camera.getYRadius() * 2).add(camera.getPosition()).sub(camera.getXRadius(), camera.getYRadius());
		if (event.getPressedKey() == EnumMouseKey.PRIMARY) {
			if (tpFirstPos == null) tpFirstPos = pos;
			else if (tpSecondPos == null) tpSecondPos = pos;
			else {
				new Teleport(new Area(tpFirstPos, tpSecondPos), pos, Game.getMap());
				Game.getMap().saveLevelEntities();
				tpFirstPos = null;
				tpSecondPos = null;
			}
		}
		if (event.getPressedKey() == EnumMouseKey.TERTIARY) {
			new ArrayList<>(Game.getMap().getLevelEntities()).stream().filter(entity -> entity instanceof Teleport)
				.filter(entity -> entity.getCurrentCollisionBox().isInside(pos)).forEach(entity -> Game.getMap().getLevelEntities().remove(entity));
			Game.getMap().saveCollisionBoxes();
		}
	}
}
