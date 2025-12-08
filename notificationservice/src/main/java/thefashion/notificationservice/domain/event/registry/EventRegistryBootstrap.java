package thefashion.notificationservice.domain.event.registry;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import thefashion.notificationservice.domain.event.handlers.EventHandler;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EventRegistryBootstrap {

    private final EventRegistry eventRegistry;
    private final List<EventHandler<?>> eventHandlers;


    @PostConstruct
    public void init() {
        eventHandlers.forEach(eventRegistry::register);
    }

}
