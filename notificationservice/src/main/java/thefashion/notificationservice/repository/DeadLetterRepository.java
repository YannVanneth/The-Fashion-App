package thefashion.notificationservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import thefashion.notificationservice.domain.model.DeadLetterNotification;

import java.time.LocalDateTime;
import java.util.List;

public interface DeadLetterRepository extends MongoRepository<DeadLetterNotification,String> {

    List<DeadLetterNotification> findByRetriedFalse();
    List<DeadLetterNotification> findByOriginalNotification(String id);

}
