package thefashion.notificationservice.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import thefashion.notificationservice.domain.enums.Channel;
import thefashion.notificationservice.domain.model.Notification;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationProducer {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public void sendToQueue(Notification notification) throws  Exception {
        String topic = resolveTopic(notification.getChannel());


        kafkaTemplate.send(topic, notification.getId(),notification)
                .whenComplete((result,ex) -> {
                   if(ex != null) {
                       throw new RuntimeException("Kafka publish failed",ex);
                   }
                });


    }

    private String resolveTopic(Channel type) {
        return switch (type) {
            case SMS -> "notification.sms";
            case EMAIL -> "notification.email";
            case PUSH -> "notification.push";
        };
    }






}
