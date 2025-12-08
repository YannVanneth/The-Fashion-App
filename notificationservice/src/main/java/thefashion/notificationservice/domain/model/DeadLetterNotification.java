package thefashion.notificationservice.domain.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import thefashion.notificationservice.domain.enums.DLQStatus;

import java.time.LocalDateTime;

@Document(collection = "dead_letter_queue")
@Data
@Builder
public class DeadLetterNotification {
    @Id
    private String id;
    private Notification originalNotification;
    private String failureReason;
    private DLQStatus status;
    private String originalTopics;
    private Long originalOffset;
    private Integer originalPartition;
    private LocalDateTime failureAt;
    private Integer retryCount;
    private boolean retried;
    private LocalDateTime retriedAt;
}
