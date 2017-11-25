package com.degoos.processing.engine.event;

public enum EnumEventPriority {

	VERY_HIGH(0), HIGH(1), NORMAL(2), LOW(3), VERY_LOW(4);

	int priority;


	EnumEventPriority(int priority) {
		this.priority = priority;
	}


	public int getPriority() {
		return priority;
	}

}
