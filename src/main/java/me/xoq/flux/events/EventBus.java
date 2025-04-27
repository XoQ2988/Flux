package me.xoq.flux.events;

import me.xoq.flux.FluxClient;

import java.lang.reflect.Method;
import java.util.*;

// Scans listener objects for @EventHandler methods and dispatches events to them.
public class EventBus {
    private final Map<Class<?>, List<ListenerMethod>> listeners = new HashMap<>();

    // Holds a target instance and its listener method
    private record ListenerMethod(Object target, Method method) {}

    // Dispatches the event to all registered listener methods
    public <E> E dispatch(E event) {
        List<ListenerMethod> regs = listeners.get(event.getClass());
        if (regs == null) return event;

        // Iterate a snapshot so unregister() can safely remove from the live list
        for (ListenerMethod lm : List.copyOf(regs)) {
            try {
                lm.method().invoke(lm.target(), event);
            } catch (Exception ex) {
                FluxClient.LOG.error("Error in event handler {}::{}",
                        lm.target().getClass().getSimpleName(),
                        lm.method().getName(), ex);
            }
        }
        return event;
    }

    // Register all @EventHandler methods on the given listener object
    public void register(Object listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(EventHandler.class)) continue;
            Class<?> eventType = method.getParameterTypes()[0];
            method.setAccessible(true);
            listeners
                    .computeIfAbsent(eventType, k -> new ArrayList<>())
                    .add(new ListenerMethod(listener, method));
        }
    }

    // Unregister all @EventHandler methods on the given listener object
    public void unregister(Object listener) {
        for (Iterator<Map.Entry<Class<?>, List<ListenerMethod>>> it = listeners.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Class<?>, List<ListenerMethod>> entry = it.next();
            List<ListenerMethod> methods = entry.getValue();
            methods.removeIf(lm -> lm.target() == listener);
            if (methods.isEmpty()) {
                it.remove();
            }
        }
    }
}