package thefashion.notificationservice.domain.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import thefashion.notificationservice.domain.dto.NotificationRequest;
import thefashion.notificationservice.domain.enums.Channel;
import thefashion.notificationservice.domain.enums.NotificationStatus;
import thefashion.notificationservice.domain.event.handlers.ChannelHandler;
import thefashion.notificationservice.domain.model.DeadLetterNotification;
import thefashion.notificationservice.domain.model.Notification;
import thefashion.notificationservice.repository.DeadLetterRepository;
import thefashion.notificationservice.repository.NotificationRepository;
import thefashion.notificationservice.domain.service.interfaces.NotificationSender;
import thefashion.notificationservice.domain.service.interfaces.NotificationSenderFactory;
import thefashion.notificationservice.domain.service.interfaces.SchedulableNotificationSender;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ChannelProcessor {

    private final Map<Channel, ChannelHandler> handlerMap
            = new EnumMap<>(Channel.class);

    public ChannelProcessor(List<ChannelHandler> handlers) {
        for (ChannelHandler handler : handlers) {
            handlerMap.put(handler.channel(),handler);
        }
    }

    public void process(NotificationRequest request) {
        ChannelHandler handler = handlerMap.get(request.channelType());


        if(handler == null) {
            throw new IllegalStateException("No handler for channel:" + request.channelType());
        }

        handler.send(request);

    }




}
