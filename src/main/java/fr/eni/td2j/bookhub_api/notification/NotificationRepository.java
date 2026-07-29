package fr.eni.td2j.bookhub_api.notification;

import fr.eni.td2j.bookhub_api.feature.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);
}
