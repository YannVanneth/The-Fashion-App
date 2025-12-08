package thefashion.notificationservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import thefashion.notificationservice.domain.enums.NotificationStatus;
import thefashion.notificationservice.domain.model.Notification;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification,String> {


    @Query("""
            SELECT n from Notification n,
            WHERE n.status = 'PENDING' 
            AND n.scheduledAt >= CURRENT_TIMESTAMP
            """)
    List<Notification> findReadyToSend();

    List<Notification> findByStatusAndRetryCountLessThan( NotificationStatus status, int maxRetry);
}
