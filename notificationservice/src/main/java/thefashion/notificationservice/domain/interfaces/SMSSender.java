package thefashion.notificationservice.domain.interfaces;

import thefashion.notificationservice.domain.dto.NotificationRequest;

public interface SMSSender {
    void sendSms(NotificationRequest request);
}
