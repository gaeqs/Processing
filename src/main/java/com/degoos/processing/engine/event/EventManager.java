package com.degoos.processing.engine.event;


import com.degoos.processing.engine.util.Validate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class EventManager {

	private static EventManager instance = new EventManager();
	private Set<EventListener> listeners;


	private EventManager() {
		listeners = Collections.newSetFromMap(new ConcurrentHashMap<>());
	}


	public static EventManager getInstance() {
		return instance;
	}


	public void registerListener(Object listener) {
		try {
			Validate.notNull(listener, "GameTestListener cannot be null!");
			listeners.addAll(Arrays.stream(listener.getClass().getMethods())
				.filter(method -> method.getAnnotation(Listener.class) != null && method.getParameterTypes().length == 1 &&
					GEvent.class.isAssignableFrom(method.getParameterTypes()[0]) && !method.isSynthetic() && !method.isBridge())
				.map(method -> new EventListener(listener, method, (Class<? extends GEvent>) method.getParameterTypes()[0])).collect(Collectors.toSet()));
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}


	public void unregisterListener(Object listener) {
		try {
			Validate.notNull(listener, "GameTestListener cannot be null!");
			new HashSet<>(listeners).stream().filter(entry -> entry.getListener().getClass().equals(listener.getClass())).forEach(listeners::remove);
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}


	public void unregisterListener(Class<?> listener) {
		try {
			Validate.notNull(listener, "GameTestListener cannot be null!");
			new HashSet<>(listeners).stream().filter(entry -> listener.isInstance(entry.getListener())).forEach(listeners::remove);
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	public void callEvent(GEvent event) {
		try {
			Validate.notNull(event, "GEvent cannot be null!");
			List<EventListener> entries = new ArrayList<>();
			listeners.stream().filter(entry -> entry.getMethod().getParameterTypes()[0].isInstance(event)).forEach(entries::add);
			boolean cancelled = false, isCancellable = event instanceof Cancellable;
			Cancellable cancellable = isCancellable ? (Cancellable) event : null;
			entries.sort(Comparator.comparingInt(o -> o.getMethod().getDeclaredAnnotation(Listener.class).priority().getPriority()));
			for (EventListener listener : entries) {
				try {
					if (cancelled && !listener.isIgnoreCancelled()) continue;
				} catch (Throwable ex) {
					ex.printStackTrace();
				}
				try {
					listener.execute(event);
				} catch (Throwable ex) {
					ex.printStackTrace();
				}
				if (isCancellable && !cancelled) cancelled = cancellable.isCancelled();
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

}
