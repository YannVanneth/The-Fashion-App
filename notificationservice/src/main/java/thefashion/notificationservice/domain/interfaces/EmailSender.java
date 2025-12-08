package thefashion.notificationservice.domain.interfaces;

import thefashion.notificationservice.domain.dto.NotificationRequest;

public interface EmailSender {
    void sendEmail(NotificationRequest request);
}
