package thefashion.notificationservice.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class SMSNotificationRequest extends NotificationRequest{
    private String phoneNumber;

    @Override
    public boolean validate() {
        return StringUtils.hasText(phoneNumber);
    }
}
