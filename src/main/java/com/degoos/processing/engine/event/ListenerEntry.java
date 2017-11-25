package com.degoos.processing.engine.event;


import java.lang.reflect.Method;

public class ListenerEntry {

	private Method method;
	private Object listener;


	public ListenerEntry (Method method, Object listener) {
		this.method = method;
		this.listener = listener;
	}


	public Method getMethod () {
		return method;
	}


	public Object getListener () {
		return listener;
	}
}
