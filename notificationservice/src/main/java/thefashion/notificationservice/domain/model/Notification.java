package thefashion.notificationservice.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import thefashion.notificationservice.domain.enums.Channel;
import thefashion.notificationservice.domain.enums.NotificationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
public class Notification {

   @Id
   private String id;
   private String userId;
   private String recipient;
   private String vendorId;
   private String subject;
   private String content;
   private Channel channel;


   private Map<String,Object> metadata;
   private NotificationStatus status;
   private Integer retryCount;
   private String errorMessage;

   private  LocalDateTime scheduledAt;
   private  LocalDateTime sentAt;

}
