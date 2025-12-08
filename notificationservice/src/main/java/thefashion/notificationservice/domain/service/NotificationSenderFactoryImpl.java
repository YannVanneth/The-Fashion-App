package thefashion.notificationservice.domain.service;

import org.springframework.stereotype.Service;
import thefashion.notificationservice.domain.enums.Channel;
import thefashion.notificationservice.domain.service.interfaces.NotificationSender;
import thefashion.notificationservice.domain.service.interfaces.NotificationSenderFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationSenderFactoryImpl implements NotificationSenderFactory {

    private final Map<Channel, NotificationSender> senderMap;

    public NotificationSenderFactoryImpl(List<NotificationSender> senders) {
        this.senderMap = senders.stream().collect(Collectors.toMap(
                NotificationSender::getChannel,
                sender -> sender
        ));
    }

    @Override
    public NotificationSender getSender(Channel channel) {
        NotificationSender sender = senderMap.get(channel);
        if(sender == null) {
            throw new IllegalArgumentException("No sender available for channel:" + channel);
        }
        return sender;
    }

    @Override
    public boolean isSupported(Channel channel) {
        return senderMap.containsKey(channel);
    }

    @Override
    public Set<Channel> getSupportsChannel() {
        return senderMap.keySet();
    }
}
