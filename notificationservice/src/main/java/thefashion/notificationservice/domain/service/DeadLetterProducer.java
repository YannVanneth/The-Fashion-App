package thefashion.notificationservice.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeadLetterProducer {

    private final KafkaTemplate<String,Object> kafkaTemplate;






}
