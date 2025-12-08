package thefashion.notificationservice.domain.dto;

import java.time.LocalDateTime;


public record SendResult(
        boolean success,
        String messageId,
        String errorMessage,
        LocalDateTime sentAt

) { }
