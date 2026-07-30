package fr.eni.td2j.bookhub_api.notification;

import fr.eni.td2j.bookhub_api.common.ApiResponse;
import fr.eni.td2j.bookhub_api.notification.dto.NotificationDTO;
import fr.eni.td2j.bookhub_api.notification.dto.NotificationReadDTO;
import fr.eni.td2j.bookhub_api.notification.dto.NotificationResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static fr.eni.td2j.bookhub_api.common.ApiResponse.success;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    public NotificationController(NotificationService notificationService, NotificationMapper notificationMapper) {
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
    }

    @GetMapping("/me")
    public ResponseEntity<List<NotificationResponseDTO>> listByUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(notificationService.findByConnectedUser(userDetails));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> detail(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.findById(id));
    }

    @PostMapping
    public ResponseEntity<NotificationResponseDTO> create(@RequestBody NotificationDTO notificationDTO, @AuthenticationPrincipal UserDetails userDetails) {
        Notification notification = notificationService.create(notificationDTO, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationMapper.toDto(notification));
    }

    @PatchMapping("/read")
    public ResponseEntity<NotificationResponseDTO> read(@RequestBody NotificationReadDTO notificationReadDTO, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(notificationService.isRead(notificationReadDTO, userDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        notificationService.delete(id, userDetails);
        return ResponseEntity.ok(ApiResponse.success(null, "La notification d'id : " + id + ", a été supprimée avec succès"));
    }

}
