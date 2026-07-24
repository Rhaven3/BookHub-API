package fr.eni.td2j.bookhub_api.notification;

import fr.eni.td2j.bookhub_api.common.BaseEntity;
import fr.eni.td2j.bookhub_api.feature.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
public class Notification extends BaseEntity {
    private NotificationEnum type;
    private String message;
    private LocalDateTime date;
    @ManyToOne
    private User user;
}
