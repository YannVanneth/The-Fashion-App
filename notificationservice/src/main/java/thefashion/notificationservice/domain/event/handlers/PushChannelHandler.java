package thefashion.notificationservice.domain.event.handlers;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thefashion.notificationservice.domain.dto.NotificationRequest;
import thefashion.notificationservice.domain.enums.Channel;
import thefashion.notificationservice.domain.interfaces.PushSender;

@Service
@RequiredArgsConstructor
public class PushChannelHandler implements ChannelHandler{

    private final PushSender sender;


    @Override
    public Channel channel() {
        return Channel.PUSH;
    }

    @Override
    public void send(NotificationRequest request) {
        sender.sendPush(request);

    }
}
