package thefashion.notificationservice.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import thefashion.notificationservice.domain.enums.Channel;
import thefashion.notificationservice.domain.model.Notification;
import thefashion.notificationservice.domain.service.interfaces.SchedulableNotificationSender;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Service
public class PushNotificationSender  implements SchedulableNotificationSender {

    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    @Override
    public void scheduleNotification(Notification notification) {
        if(notification == null || notification.getScheduledAt() == null) {
            throw new IllegalArgumentException("Notification and scheduled time cannot be null");
        }
        if(notification.getScheduledAt().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Notification and scheduled time must be in future");
        }

        long delay = Duration.between(
                LocalDateTime.now(),
                notification.getScheduledAt()
        ).toMillis();


        ScheduledFuture<?> future = scheduler.schedule(
                () -> {
                    try{
                        send(notification);
                        scheduledTasks.remove(notification.getId());
                    } catch (Exception e) {
                        log.error("Failed to send scheduled push notification: {}", notification.getId(),e);
                    }
                },
                delay,
                TimeUnit.MILLISECONDS
        );


        scheduledTasks.put(notification.getId(),future);
        log.info("Push notification scheduled: {} at {}", notification.getId(), notification.getScheduledAt());
    }

    @Override
    public boolean cancelSchedule(String notificationId) {
        if(notificationId == null || notificationId.isEmpty()){
            return false;
        }

        ScheduledFuture<?> future = scheduledTasks.remove(notificationId);
        if(future != null && !future.isDone()){
            boolean cancelled = future.cancel(false);
            return cancelled;
        }
        return false;

    }

    @Override
    public boolean reschedule(String notificationId, LocalDateTime newTime) {
        if(notificationId == null || newTime == null) {
            return false;
        }
        if(newTime.isBefore(LocalDateTime.now())){
            return false;
        }

        ScheduledFuture<?> existingFuture = scheduledTasks.get(notificationId);
        if(existingFuture == null) {
            return false;
        }

        if(!cancelSchedule(notificationId)){
            return false;
        }

        return true;
    }

    @Override
    public boolean send(Notification notification) {
        if(!validate(notification)) {
            return false;
        }
        try {
            Thread.sleep(100);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public boolean validate(Notification notification) {

        if(notification == null) {
            return false;
        }

        if(notification.getRecipient() == null || notification.getRecipient().isEmpty()) {
            return false;
        }

        if(notification.getContent() == null || notification.getContent().isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    public Channel getChannel() {
        return Channel.PUSH;
    }

    protected  int getScheduledTaskCount() {
        return scheduledTasks.size();
    }
}

