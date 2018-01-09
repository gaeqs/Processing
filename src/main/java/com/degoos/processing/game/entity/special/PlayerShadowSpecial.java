package com.degoos.processing.game.entity.special;

import com.degoos.processing.game.entity.Player;

public class PlayerShadowSpecial implements Special {

	private long lastUse = 0;
	private int phase = 0;
	private Player player;

	public PlayerShadowSpecial(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		if (getActiveCooldown() > 0) return;
		lastUse = System.currentTimeMillis();
		phase = 1;
	}

	@Override
	public void onTick(long dif) {
		float opacity;
		switch (phase) {
			case 1:
				opacity = Math.max(0, player.getImageOpacity() - (0.01F * dif));
				player.setFullOpacity(opacity);
				if (opacity == 0) phase = 2;
				break;
			case 2:
				if (System.currentTimeMillis() - lastUse > 500) phase = 3;
				break;
			case 3:
				opacity = Math.min(1, player.getImageOpacity() + (0.01F * dif));
				player.setFullOpacity(opacity);
				if (opacity == 1) phase = 0;
				break;
			default:
				break;
		}
	}

	@Override
	public boolean isActive() {
		return phase != 0;
	}

	@Override
	public long getCooldown() {
		return 3000;
	}

	@Override
	public long getActiveCooldown() {
		return Math.max(0, lastUse - System.currentTimeMillis() + getCooldown());
	}
}
