package thefashion.notificationservice.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import thefashion.notificationservice.domain.enums.Channel;
import thefashion.notificationservice.domain.enums.NotificationStatus;
import thefashion.notificationservice.domain.event.NotificationTaskScheduler;
import thefashion.notificationservice.domain.model.Notification;
import thefashion.notificationservice.repository.NotificationRepository;
import thefashion.notificationservice.domain.service.interfaces.NotificationSender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationTaskSchedulerTest {

    @Mock
    private NotificationRepository repository;

    @Mock
    private NotificationSender sender;

    @InjectMocks
    private NotificationTaskScheduler scheduler;

    private Notification notification;

    @BeforeEach
    void setup() {
        notification = Notification.builder()
                .id("1")
                .recipient("test@email.com")
                .subject("Test Subject")
                .content("Test message")
                .userId("user-123")
                .channel(Channel.EMAIL)
                .metadata(Map.of())
                .status(NotificationStatus.PENDING)
                .retryCount(0)
                .errorMessage(null)
                .scheduledAt(LocalDateTime.now().minusSeconds(5))
                .sentAt(null)
                .build();
    }

    // =========================
    // ✅ SUCCESS PATH
    // =========================
    @Test
    void shouldSendNotificationAndMarkAsSent() {

        when(repository.findReadyToSend())
                .thenReturn(List.of(notification));

        scheduler.dispatchPendingNotification();

        verify(sender, times(1)).send(notification);
        assertEquals(NotificationStatus.SENT, notification.getStatus());
        assertEquals(0, notification.getRetryCount());
    }

    // =========================
    // ✅ RETRY PATH
    // =========================
    @Test
    void shouldIncreaseRetryCountWhenSendFails() {

        when(repository.findReadyToSend())
                .thenReturn(List.of(notification));

        doThrow(new RuntimeException("Provider down"))
                .when(sender).send(notification);

        scheduler.dispatchPendingNotification();

        assertEquals(1, notification.getRetryCount());
        assertEquals(NotificationStatus.PENDING, notification.getStatus());
    }

    // =========================
    // ✅ FINAL FAILURE PATH
    // =========================
    @Test
    void shouldMarkAsFailedWhenMaxRetryReached() {

        notification.setRetryCount(3); // MAX_RETRY = 3

        when(repository.findReadyToSend())
                .thenReturn(List.of(notification));

        doThrow(new RuntimeException("Permanent failure"))
                .when(sender).send(notification);

        scheduler.dispatchPendingNotification();

        assertEquals(NotificationStatus.FAILED, notification.getStatus());
        assertEquals(4, notification.getRetryCount());
    }

    // =========================
    // ✅ EMPTY QUEUE PATH
    // =========================
    @Test
    void shouldDoNothingWhenNoPendingNotifications() {

        when(repository.findReadyToSend())
                .thenReturn(List.of());

        scheduler.dispatchPendingNotification();

        verify(sender, never()).send(any());
        verify(repository, times(1)).findReadyToSend();
    }

    // =========================
    // ✅ MULTIPLE NOTIFICATIONS
    // =========================
    @Test
    void shouldProcessMultipleNotificationsIndependently() {

        Notification second = Notification.builder()
                .id("2")
                .recipient("second@email.com")
                .subject("Test Subject")
                .content("Second message")
                .userId("user-123")
                .channel(Channel.EMAIL)
                .metadata(Map.of())
                .status(NotificationStatus.PENDING)
                .retryCount(0)
                .errorMessage(null)
                .scheduledAt(LocalDateTime.now().minusSeconds(5))
                .sentAt(null)
                .build();

        when(repository.findReadyToSend())
                .thenReturn(List.of(notification, second));

        doThrow(new RuntimeException("Fail first"))
                .when(sender).send(notification);

        scheduler.dispatchPendingNotification();

        assertEquals(NotificationStatus.PENDING, notification.getStatus());
        assertEquals(1, notification.getRetryCount());

        assertEquals(NotificationStatus.SENT, second.getStatus());
    }

    // =========================
    // ✅ NULL NOTIFICATION HANDLING
    // =========================
    @Test
    void shouldHandleNullNotificationGracefully() {

        when(repository.findReadyToSend())
                .thenReturn(List.of((Notification) null));

        assertDoesNotThrow(() -> scheduler.dispatchPendingNotification());

        verify(sender, never()).send(any());
    }

    // =========================
    // ✅ NOTIFICATION NOT FOUND
    // =========================
    @Test
    void shouldHandleNotificationNotFoundError() {

        Notification deleted = Notification.builder()
                .id("999")
                .recipient("deleted@email.com")
                .subject("Test Subject")
                .content("Deleted message")
                .userId("user-123")
                .channel(Channel.EMAIL)
                .metadata(Map.of())
                .status(NotificationStatus.PENDING)
                .retryCount(0)
                .errorMessage(null)
                .scheduledAt(LocalDateTime.now().minusSeconds(5))
                .sentAt(null)
                .build();

        when(repository.findReadyToSend())
                .thenReturn(List.of(deleted));

        doThrow(new IllegalArgumentException("Notification not found"))
                .when(sender).send(deleted);

        assertDoesNotThrow(() -> scheduler.dispatchPendingNotification());

        verify(sender, times(1)).send(deleted);
    }

    // =========================
    // ✅ INVALID RECIPIENT
    // =========================
    @Test
    void shouldHandleInvalidRecipient() {

        Notification invalidRecipient = Notification.builder()
                .id("1")
                .recipient(null)
                .subject("Test Subject")
                .content("Test message")
                .userId("user-123")
                .channel(Channel.EMAIL)
                .metadata(Map.of())
                .status(NotificationStatus.PENDING)
                .retryCount(0)
                .errorMessage(null)
                .scheduledAt(LocalDateTime.now().minusSeconds(5))
                .sentAt(null)
                .build();

        when(repository.findReadyToSend())
                .thenReturn(List.of(invalidRecipient));

        doThrow(new IllegalArgumentException("Invalid recipient"))
                .when(sender).send(invalidRecipient);

        scheduler.dispatchPendingNotification();

        assertEquals(NotificationStatus.PENDING, invalidRecipient.getStatus());
        assertEquals(1, invalidRecipient.getRetryCount());
    }

    // =========================
    // ✅ EMPTY MESSAGE
    // =========================
    @Test
    void shouldHandleEmptyMessage() {

        Notification emptyMessage = Notification.builder()
                .id("1")
                .recipient("test@email.com")
                .subject("Test Subject")
                .content("")
                .userId("user-123")
                .channel(Channel.EMAIL)
                .metadata(Map.of())
                .status(NotificationStatus.PENDING)
                .retryCount(0)
                .errorMessage(null)
                .scheduledAt(LocalDateTime.now().minusSeconds(5))
                .sentAt(null)
                .build();

        when(repository.findReadyToSend())
                .thenReturn(List.of(emptyMessage));

        doThrow(new IllegalArgumentException("Empty message"))
                .when(sender).send(emptyMessage);

        scheduler.dispatchPendingNotification();

        assertEquals(NotificationStatus.PENDING, emptyMessage.getStatus());
        assertEquals(1, emptyMessage.getRetryCount());
    }


    // =========================
    // ✅ ALREADY SENT NOTIFICATION
    // =========================
    @Test
    void shouldNotProcessAlreadySentNotification() {

        Notification alreadySent = Notification.builder()
                .id("1")
                .recipient("test@email.com")
                .subject("Test Subject")
                .content("Test message")
                .userId("user-123")
                .channel(Channel.EMAIL)
                .metadata(Map.of())
                .status(NotificationStatus.SENT)
                .retryCount(0)
                .errorMessage(null)
                .scheduledAt(LocalDateTime.now().minusSeconds(5))
                .sentAt(LocalDateTime.now())
                .build();

        when(repository.findReadyToSend())
                .thenReturn(List.of());

        scheduler.dispatchPendingNotification();

        verify(sender, never()).send(alreadySent);
    }

    // =========================
    // ✅ REPOSITORY EXCEPTION
    // =========================
    @Test
    void shouldHandleRepositoryException() {

        when(repository.findReadyToSend())
                .thenThrow(new RuntimeException("Database connection lost"));

        assertDoesNotThrow(() -> scheduler.dispatchPendingNotification());

        verify(sender, never()).send(any());
    }

    // =========================
    // ✅ CONCURRENT MODIFICATION
    // =========================
    @Test
    void shouldHandleConcurrentStatusChange() {

        when(repository.findReadyToSend())
                .thenReturn(List.of(notification));

        doAnswer(invocation -> {
            // Simulate concurrent modification - notification processed elsewhere
            throw new IllegalStateException("Notification already processed");
        }).when(sender).send(notification);

        assertDoesNotThrow(() -> scheduler.dispatchPendingNotification());

        verify(sender, times(1)).send(notification);
    }

    // =========================
    // ✅ BATCH PROCESSING
    // =========================
    @Test
    void shouldProcessLargeBatchEfficiently() {

        List<Notification> batch = List.of(
                createNotification(1L),
                createNotification(2L),
                createNotification(3L),
                createNotification(4L),
                createNotification(5L)
        );

        when(repository.findReadyToSend())
                .thenReturn(batch);

        scheduler.dispatchPendingNotification();

        verify(sender, times(5)).send(any(Notification.class));
    }

    // =========================
    // HELPER METHODS
    // =========================
    private Notification createNotification(Long id) {
        return Notification.builder()
                .id(String.valueOf(id))
                .recipient("user" + id + "@email.com")
                .subject("Test Subject " + id)
                .content("Message " + id)
                .userId("user-" + id)
                .channel(Channel.EMAIL)
                .metadata(Map.of())
                .status(NotificationStatus.PENDING)
                .retryCount(0)
                .errorMessage(null)
                .scheduledAt(LocalDateTime.now().minusSeconds(5))
                .sentAt(null)
                .build();
    }
}