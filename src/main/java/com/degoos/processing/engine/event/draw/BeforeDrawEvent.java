package com.degoos.processing.engine.event.draw;

import com.degoos.processing.engine.event.GEvent;

public class BeforeDrawEvent extends GEvent {

	private long dif;

	public BeforeDrawEvent(long dif) {
		this.dif = dif;
	}

	public long getDif() {
		return dif;
	}
}
