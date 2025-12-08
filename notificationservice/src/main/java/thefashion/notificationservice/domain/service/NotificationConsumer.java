package thefashion.notificationservice.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import thefashion.notificationservice.domain.dto.NotificationRequest;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {
    private final ChannelProcessor processor;


    @KafkaListener(
            topics = "notification.email",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeEmail(NotificationRequest request) {
        processor.process(request);
    }


    @KafkaListener(
            topics = "notification.push",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumePush(NotificationRequest request) {
        processor.process(request);
    }


    @KafkaListener(
            topics = "notification.sms",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeSms(NotificationRequest request) {
        processor.process(request);
    }
}
