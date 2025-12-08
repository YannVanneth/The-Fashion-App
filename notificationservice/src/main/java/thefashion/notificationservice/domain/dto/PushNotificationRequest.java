package thefashion.notificationservice.domain.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class PushNotificationRequest extends NotificationRequest {

    private String title;
    private Map<String,String> data = new HashMap<>();
    private String imageUrl;

    @Override
    public boolean validate() {
        return StringUtils.hasText(title);
    }
}
