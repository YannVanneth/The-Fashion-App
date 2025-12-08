package thefashion.notificationservice.domain.interfaces;

import thefashion.notificationservice.domain.dto.NotificationRequest;

public interface PushSender {
    void sendPush(NotificationRequest request);
}
