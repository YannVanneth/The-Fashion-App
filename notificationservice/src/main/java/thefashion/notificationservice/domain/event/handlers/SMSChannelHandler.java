package thefashion.notificationservice.domain.event.handlers;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thefashion.notificationservice.domain.dto.NotificationRequest;
import thefashion.notificationservice.domain.enums.Channel;
import thefashion.notificationservice.domain.interfaces.SMSSender;

@Service
@RequiredArgsConstructor
public class SMSChannelHandler implements ChannelHandler {

    private final SMSSender sender;


    @Override
    public Channel channel() {
        return Channel.SMS;
    }

    @Override
    public void send(NotificationRequest request) {
        sender.sendSms(request);
    }
}
