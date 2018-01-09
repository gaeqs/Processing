package com.degoos.processing.game.entity.special;

public interface Special {

	void execute();

	void onTick(long dif);

	boolean isActive ();

	long getCooldown ();

	long getActiveCooldown ();

}
