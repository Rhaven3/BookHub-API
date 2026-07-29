package fr.eni.td2j.bookhub_api.notification.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NotificationDTO {
    private String message;
    private String type;
    private Long userId;
}
