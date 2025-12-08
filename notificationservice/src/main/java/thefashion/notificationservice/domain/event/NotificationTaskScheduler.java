package thefashion.notificationservice.domain.event;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import thefashion.notificationservice.domain.enums.NotificationStatus;
import thefashion.notificationservice.domain.model.Notification;
import thefashion.notificationservice.repository.NotificationRepository;
import thefashion.notificationservice.domain.service.interfaces.NotificationSender;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationTaskScheduler {

    private final NotificationRepository repository;
    private final NotificationSender sender;

    private static int MAX_RETRY = 5;



    @Scheduled(fixedDelay = 3000)
    @Async("notificationSchedulerExecutor")
    @Transactional
    public void dispatchPendingNotification() {
        List<Notification> readyList =
                repository.findReadyToSend();

        if(readyList.isEmpty()) return;

        for (Notification notification: readyList) {
            try{
                sender.send(notification);
                notification.setStatus(NotificationStatus.SENT);
            } catch (Exception e) {
                notification.setRetryCount(
                        notification.getRetryCount() + 1);
                if(notification.getRetryCount() >= MAX_RETRY){
                    notification.setStatus(NotificationStatus.FAILED);
                }
                throw new RuntimeException(e);
            }
        }
    }

}
