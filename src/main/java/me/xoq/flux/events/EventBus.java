package me.xoq.flux.events;

import me.xoq.flux.FluxClient;

import java.lang.reflect.Method;
import java.util.*;

// Scans listener objects for @EventHandler methods and dispatches events to them.
public class EventBus {
    private final Map<Class<?>, List<ListenerMethod>> listeners = new HashMap<>();

    // Holds a target instance and its listener method
    private record ListenerMethod(Object target, Method method) {}

    // Register all @EventHandler methods on the given listener object
    public void register(Object listener) {
        for (Method m : listener.getClass().getDeclaredMethods()) {
            if (!m.isAnnotationPresent(EventHandler.class)) continue;
            if (m.getParameterCount() != 1) {
                FluxClient.LOG.warn("Invalid @EventHandler method (must take one arg): {}::{}",
                        listener.getClass().getSimpleName(), m.getName());
                continue;
            }
            Class<?> eventType = m.getParameterTypes()[0];
            m.setAccessible(true);
            listeners
                    .computeIfAbsent(eventType, k -> new ArrayList<>())
                    .add(new ListenerMethod(listener, m));
        }
    }

    // Unregister all @EventHandler methods on the given listener object
    public void unregister(Object listener) {
        for (Iterator<Map.Entry<Class<?>, List<ListenerMethod>>> it = listeners.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Class<?>, List<ListenerMethod>> entry = it.next();
            List<ListenerMethod> methods = entry.getValue();
            methods.removeIf(lm -> lm.target == listener);
            if (methods.isEmpty()) {
                it.remove();
            }
        }
    }

    // Dispatches the event to all registered listener methods
    public <T> T dispatch(T event) {
        Class<?> cls = event.getClass();
        for (var entry : listeners.entrySet()) {
            if (!entry.getKey().isAssignableFrom(cls)) continue;
            for (ListenerMethod lm : entry.getValue()) {
                try {
                    lm.method.invoke(lm.target, event);
                } catch (Exception e) {
                    FluxClient.LOG.error("Error in handler {}::{} for {}",
                            lm.target.getClass().getSimpleName(),
                            lm.method.getName(),
                            cls.getSimpleName(), e);
                }
            }
        }
        return event;
    }
}