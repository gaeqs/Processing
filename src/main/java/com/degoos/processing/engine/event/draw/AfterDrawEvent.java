package com.degoos.processing.engine.event.draw;

import com.degoos.processing.engine.event.GEvent;

public class AfterDrawEvent extends GEvent {

	private long dif;

	public AfterDrawEvent(long dif) {
		this.dif = dif;
	}

	public long getDif() {
		return dif;
	}
}
