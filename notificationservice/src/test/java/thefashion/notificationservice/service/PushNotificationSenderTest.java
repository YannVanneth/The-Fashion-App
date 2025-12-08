package thefashion.notificationservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import thefashion.notificationservice.domain.enums.Channel;
import thefashion.notificationservice.domain.model.Notification;
import thefashion.notificationservice.domain.service.PushNotificationSender;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PushNotificationSenderTest {

    private PushNotificationSender sender;
    private Notification notification;


    @BeforeEach
    void setup() {
        sender = new PushNotificationSender();
    }

    @Test
    void getChannel_returnsPushChannel(){
        Channel result = sender.getChannel();


        assertEquals(Channel.PUSH,result);
    }


    @Test
    void send_withValidNotification_returnsTrue() {
        notification = createValidNotification();


        boolean result = sender.send(notification);
        assertTrue(result);
    }

    @Test
    void send_withValidNotification_returnsFalse() {
        notification = createValidNotification();
        notification.setRecipient(null);

        boolean result = sender.send(notification);
        assertFalse(result);
    }

    @Test
    void scheduleNotification_withValidFutureTime_scheduleSuccessfully() {
        notification = createValidNotification();
        notification.setScheduledAt(LocalDateTime.now().plusMinutes(5));

        assertDoesNotThrow(() -> sender.scheduleNotification(notification));
        assertEquals(1, sender.getScheduledTaskCount());
    }



    @Test
    void scheduleNotification_withNullNotification_throwExceptions(){
        assertThrows(IllegalArgumentException.class, ()-> sender.scheduleNotification(null));
    }

    @Test
    void scheduleNotification_withNullScheduledTime_throwExceptions() {
       notification = createValidNotification();
       notification.setScheduledAt(null);
       assertThrows(IllegalArgumentException.class, () -> sender.scheduleNotification(notification));

    }

//FIXME: Someting went wrong
    @Test
    void reschedule_withValidParameters_returnsTrue() {
        notification = createValidNotification();
        String notificationId = notification.getId();
        notification.setScheduledAt(LocalDateTime.now().minusMinutes(5));
        sender.scheduleNotification(notification);
        LocalDateTime newTime = LocalDateTime.now().plusMinutes(10);
        boolean result = sender.reschedule(notificationId, newTime);

        assertTrue(result);

    }

    @Test
    void reschedule_withNullId_returnFalse() {

        boolean result = sender.reschedule(null,LocalDateTime.now().plusMinutes(5));
        assertFalse(result);
    }


    @Test
    void reschedule_withNullTime_returnFalse() {

        notification = createValidNotification();
        notification.setScheduledAt(LocalDateTime.now().plusMinutes(5));
        sender.scheduleNotification(notification);
        boolean result = sender.reschedule(notification.getId(),null);

        assertFalse(result);
    }
    private Notification createValidNotification() {
        notification = Notification.builder()
                .id("test-id")
                .recipient("devce-1")
                .subject("ateskjkajsk")
                .content("Test Content")
                .build();
        return notification;
    }


}
