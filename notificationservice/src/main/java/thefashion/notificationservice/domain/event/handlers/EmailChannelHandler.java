package thefashion.notificationservice.domain.event.handlers;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thefashion.notificationservice.domain.dto.NotificationRequest;
import thefashion.notificationservice.domain.enums.Channel;
import thefashion.notificationservice.domain.interfaces.EmailSender;
import thefashion.notificationservice.domain.model.Notification;

@Service
@RequiredArgsConstructor
public class EmailChannelHandler  implements ChannelHandler{

    private final EmailSender sender;

    @Override
    public Channel channel() {
        return Channel.EMAIL;
    }

    @Override
    public void send(NotificationRequest request) {
        sender.sendEmail(request);
    }

}
