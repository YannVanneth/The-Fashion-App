package thefashion.notificationservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import thefashion.notificationservice.domain.enums.Channel;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = EmailNotificationRequest.class, name = "EMAIL"),
        @JsonSubTypes.Type(value = PushNotificationRequest.class, name = "PUSH"),
        @JsonSubTypes.Type(value = SMSNotificationRequest.class, name = "SMS")

})

public abstract class NotificationRequest {

    private String requestId;
    private Channel channel;
    private String userId;
    private String recipient;
    private String content;

    private Map<String,String> metadata = new HashMap<>();
    private LocalDateTime timestamp = LocalDateTime.now();
    public abstract boolean validate();


}
