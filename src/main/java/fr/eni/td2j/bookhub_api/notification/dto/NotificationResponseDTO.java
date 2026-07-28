package fr.eni.td2j.bookhub_api.notification.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NotificationResponseDTO {
    private String message;
    private String date;
    private String type;
    private int userId;
}
