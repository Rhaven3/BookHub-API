package fr.eni.td2j.bookhub_api.notification;

import fr.eni.td2j.bookhub_api.exception.NotFoundException;
import fr.eni.td2j.bookhub_api.feature.user.User;
import fr.eni.td2j.bookhub_api.feature.user.UserRepository;
import fr.eni.td2j.bookhub_api.feature.user.UserService;
import fr.eni.td2j.bookhub_api.notification.dto.NotificationDTO;
import fr.eni.td2j.bookhub_api.notification.dto.NotificationReadDTO;
import fr.eni.td2j.bookhub_api.notification.dto.NotificationResponseDTO;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final NotificationMapper notificationMapper;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository, UserService userService, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.notificationMapper = notificationMapper;
    }


    public List<NotificationResponseDTO> findByUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable."));
        return notificationRepository.findByUser(user).stream()
                .map(notificationMapper::toDto)
                .toList();
    }

    public List<NotificationResponseDTO> findByConnectedUser(UserDetails userDetails) {
        User user = userService.getCurrentUser(userDetails);
        if (user == null) {
            throw new NotFoundException("Utilisateur introuvable.");
        }
        return findByUser(user.getId());
    }

    public NotificationResponseDTO findById(Long id) {
        return notificationRepository.findById(id)
                .map(notificationMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Notification introuvable."));
    }

    public Notification create(@Valid NotificationDTO notificationDTO, UserDetails userDetails) {
        if (notificationDTO == null) {
            throw new IllegalArgumentException("La notification est null.");
        }
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable."));

        LocalDateTime now = LocalDateTime.now();

        Notification notification = Notification.builder()
                .date(now)
                .message(notificationDTO.getMessage())
                .user(user)
                .type(NotificationEnum.fromString(notificationDTO.getType()))
                .isRead(false)
                .build();
        return notificationRepository.save(notification);
    }

    public void delete(Long id, UserDetails userDetails) {
        Notification notification = getOwnedNotification(id, userDetails);
        notificationRepository.delete(notification);
    }

    public NotificationResponseDTO isRead(NotificationReadDTO notificationReadDTO, UserDetails userDetails) {
        Notification notification = getOwnedNotification(notificationReadDTO.getId(), userDetails);
        notification.setRead(notificationReadDTO.isRead());
        return notificationMapper.toDto(notificationRepository.save(notification));
    }

    /**
     * retourne la notification qui appartient à l'utilisateur connecter, sinon renvoie une NotFoundException
     *
     * @param id          l'id de la notification
     * @param userDetails l'utilisateur connecter
     * @return la notification
     */
    private Notification getOwnedNotification(Long id, UserDetails userDetails) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notification introuvable."));
        User user = userService.getCurrentUser(userDetails);
        if (user == null) {
            throw new NotFoundException("Utilisateur introuvable.");
        }
        if (!notification.getUser().getId().equals(user.getId()) && !user.getRole().equals("ADMIN")) {
            throw new NotFoundException("Vous ne pouvez pas supprimer cette notification.");
        }
        return notification;
    }
}
