package com.degoos.processing.game.controller;

import com.degoos.processing.game.entity.Entity;

public interface Controller {

	void onTick(long dif, Entity entity);
}
