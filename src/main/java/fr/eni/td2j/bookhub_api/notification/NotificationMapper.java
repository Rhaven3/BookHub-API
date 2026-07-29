package fr.eni.td2j.bookhub_api.notification;

import fr.eni.td2j.bookhub_api.common.EntityMapper;
import fr.eni.td2j.bookhub_api.notification.dto.NotificationResponseDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class NotificationMapper implements EntityMapper<Notification, NotificationResponseDTO> {
    @Override
    public NotificationResponseDTO toDto(Notification notification) {
        return NotificationResponseDTO.builder()
                .id(notification.getId())
                .date(notification.getDate().toString())
                .message(notification.getMessage())
                .type(
                        Optional.ofNullable(notification.getType())
                                .map(Enum::toString)
                                .orElse(null)
                )
                .userId(notification.getUser().getId())
                .build();
    }
}
