package thefashion.notificationservice.domain.event.registry;

import jdk.jfr.Event;
import thefashion.notificationservice.domain.event.DomainEvent;
import thefashion.notificationservice.domain.event.handlers.EventHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventRegistry {

    private final Map<Class<?>, List<EventHandler<?>>> handlers = new ConcurrentHashMap<>();

    public <T extends DomainEvent> void register(EventHandler<T> handler) {
        handlers.computeIfAbsent(handler.supportType(), k -> new CopyOnWriteArrayList<>())
                .add(handler);
    }

    public <T extends  DomainEvent> void dispatch(T event) {
        List<EventHandler<?>> eventHandlers =
                handlers.getOrDefault(event.getClass(),List.of());
        for(EventHandler<?> handler: eventHandlers) {
            ((EventHandler) handlers).handler(event);
        }
    }



}
