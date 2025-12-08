package thefashion.notificationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import thefashion.notificationservice.domain.event.registry.EventRegistry;

@Configuration
public class EventConfig {

    @Bean
    public EventRegistry eventRegistry() {
        return new EventRegistry();
    }

}
