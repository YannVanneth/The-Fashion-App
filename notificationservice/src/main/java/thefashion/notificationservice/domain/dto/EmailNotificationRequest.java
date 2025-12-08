package thefashion.notificationservice.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;
import thefashion.notificationservice.domain.model.Attachment;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmailNotificationRequest extends NotificationRequest{

    private String subject;
    private List<String> ccRecipients = new ArrayList<>();
    private List<Attachment> attachments = new ArrayList<>();



    @Override
    public boolean validate() {
        return StringUtils.hasText(subject);
    }
}
