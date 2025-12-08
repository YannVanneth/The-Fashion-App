package thefashion.notificationservice.domain.event.handlers;

import thefashion.notificationservice.domain.dto.NotificationRequest;
import thefashion.notificationservice.domain.enums.Channel;
import thefashion.notificationservice.domain.model.Notification;

public interface ChannelHandler {

    Channel channel();
    void send(NotificationRequest request);
}
