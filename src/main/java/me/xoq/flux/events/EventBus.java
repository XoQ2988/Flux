package me.xoq.flux.events;

import me.xoq.flux.FluxClient;

import java.lang.reflect.Method;
import java.util.*;

// Scans listener objects for @EventHandler methods and dispatches events to them.
public class EventBus {
    private final Map<Class<?>, List<ListenerMethod>> listeners = new HashMap<>();

    // Dispatches the event to all registered listener methods
    public <E> E dispatch(E event) {
        List<ListenerMethod> methods = listeners.get(event.getClass());
        if (methods == null) return event;

        // Iterate a snapshot so unregister() can safely remove from the live list
        for (ListenerMethod listenerMethod : List.copyOf(methods)) {
            try {
                listenerMethod.method.invoke(listenerMethod.target, event);
            } catch (Exception exception) {
                FluxClient.LOG.error("Error in event handler {}::{}",
                        listenerMethod.target.getClass().getSimpleName(), listenerMethod.method.getName(), exception);
            }
        }
        return event;
    }

    // Register all @EventHandler methods on the given listener object
    public void register(Object listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(EventHandler.class)) continue;

            if (method.getParameterCount() != 1) {
                FluxClient.LOG.warn(
                        "Invalid @EventHandler method (must take one arg): {}::{}",
                        listener.getClass().getSimpleName(), method.getName()
                );
                continue;
            }

            Class<?> eventType = method.getParameterTypes()[0];
            method.setAccessible(true);
            listeners
                    .computeIfAbsent(eventType, k -> new ArrayList<>())
                    .add(new ListenerMethod(listener, method));
        }
    }

    // Unregister all @EventHandler methods on the given listener object
    public void unregister(Object listener) {
        Iterator<Map.Entry<Class<?>, List<ListenerMethod>>> entryIterator = listeners.entrySet().iterator();

        while (entryIterator.hasNext()) {
            Map.Entry<Class<?>, List<ListenerMethod>> listEntry = entryIterator.next();
            List<ListenerMethod> listenerMethods = listEntry.getValue();

            // remove any methods whose target is targeted listener
            listenerMethods.removeIf(listenerMethod -> listenerMethod.target == listener);

            // if no handlers remain for this event type, remove the key
            if (listenerMethods.isEmpty()) {
                entryIterator.remove();
            }
        }
    }

    // Holds a target instance and its listener method
    public static class ListenerMethod {
        final Object target;
        final Method method;

        ListenerMethod(Object target, Method method) {
            this.target = target;
            this.method = method;
        }
    }
}