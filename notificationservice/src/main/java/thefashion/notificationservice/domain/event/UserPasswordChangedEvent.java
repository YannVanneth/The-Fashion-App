package thefashion.notificationservice.domain.event;

import java.time.Instant;

public record UserPasswordChangedEvent(
        String userId,
        Instant occurredAt

) implements DomainEvent{
}
