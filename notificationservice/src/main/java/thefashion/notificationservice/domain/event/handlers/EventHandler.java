package thefashion.notificationservice.domain.event.handlers;

import thefashion.notificationservice.domain.event.DomainEvent;

public interface EventHandler<T extends DomainEvent> {
    void handler(T event);
    Class<T> supportType();

}
