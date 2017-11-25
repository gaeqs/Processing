package com.degoos.processing.engine.event;


public interface Cancellable {

	boolean isCancelled();

	void setCancelled(boolean cancelled);

}
