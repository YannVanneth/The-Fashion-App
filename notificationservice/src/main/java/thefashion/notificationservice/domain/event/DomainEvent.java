package thefashion.notificationservice.domain.event;

import java.time.Instant;

public interface DomainEvent {

   Instant occurredAt();
}
