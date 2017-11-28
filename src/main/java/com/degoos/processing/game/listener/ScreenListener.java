package com.degoos.processing.game.listener;

import com.degoos.processing.engine.event.Listener;
import com.degoos.processing.engine.event.screen.ScreenResizeEvent;
import com.degoos.processing.game.Game;

public class ScreenListener {

	@Listener
	public void onResize (ScreenResizeEvent event) {
		Game.refreshCameraRadius(event.getNewSize());
	}

}
