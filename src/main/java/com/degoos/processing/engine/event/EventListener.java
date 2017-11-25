/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.degoos.processing.engine.event;

import java.lang.reflect.Method;

// TODO Revise this
public class EventListener {

	private final Method method;
	private final Class<? extends GEvent> eventClass;
	private final Object listener;
	private final boolean ignoreCancelled;

	/**
	 * Wraps an event executor and associates a timing handler to it. o
	 *
	 * @param method the listener method.
	 * @param eventClass the event class.
	 * @param listener the listener object.
	 */
	public EventListener(Object listener, Method method, Class<? extends GEvent> eventClass) {
		this.method = method;
		this.listener = listener;
		this.eventClass = eventClass;
		this.ignoreCancelled = method.getAnnotation(Listener.class).ignoreCancelled();
	}

	public Method getMethod() {
		return method;
	}

	public Class<? extends GEvent> getEventClass() {
		return eventClass;
	}

	public Object getListener() {
		return listener;
	}

	public boolean isIgnoreCancelled() {
		return ignoreCancelled;
	}

	public void execute(GEvent event) throws Exception {
		if (!eventClass.isInstance(event)) return;
		method.invoke(listener, event);
	}
}
