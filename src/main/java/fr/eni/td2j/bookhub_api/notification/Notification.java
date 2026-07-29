package fr.eni.td2j.bookhub_api.notification;

import fr.eni.td2j.bookhub_api.common.BaseEntity;
import fr.eni.td2j.bookhub_api.feature.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@Entity
public class Notification extends BaseEntity {
    private NotificationEnum type;
    private String message;
    private LocalDateTime date;
    private boolean isRead;
    @ManyToOne
    private User user;
}
