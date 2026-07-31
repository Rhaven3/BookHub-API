package fr.eni.td2j.bookhub_api.notification;

import fr.eni.td2j.bookhub_api.common.seeder.EntitySeeder;
import fr.eni.td2j.bookhub_api.feature.user.User;
import fr.eni.td2j.bookhub_api.feature.user.UserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificationSeeder implements EntitySeeder {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationSeeder(NotificationRepository notificationRepository,
                              UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void seed() {
        if (notificationRepository.count() > 0) return;

        User alice = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals("alice.bernard@mail.com"))
                .findFirst()
                .orElseThrow();

        User bob = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals("bob.petit@mail.com"))
                .findFirst()
                .orElseThrow();

        notificationRepository.save(Notification.builder()
                .type(NotificationEnum.WARNING)
                .message("Votre emprunt de \"Dune\" arrive à échéance dans 2 jours.")
                .date(LocalDateTime.now().minusDays(1))
                .user(bob)
                .build());

        notificationRepository.save(Notification.builder()
                .type(NotificationEnum.ALERT)
                .message("Votre emprunt de \"Dune\" est en retard.")
                .date(LocalDateTime.now())
                .user(bob)
                .build());

        notificationRepository.save(Notification.builder()
                .type(NotificationEnum.INFO)
                .message("Bienvenue sur BookHub, Alice !")
                .date(LocalDateTime.now().minusDays(10))
                .user(alice)
                .build());
    }

    @Override
    public int order() {
        return 2; // dépend uniquement de UserSeeder
    }
}
