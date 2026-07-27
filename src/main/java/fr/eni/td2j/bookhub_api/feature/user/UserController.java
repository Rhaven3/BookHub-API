package fr.eni.td2j.bookhub_api.feature.user;

import fr.eni.td2j.bookhub_api.common.ApiResponse;
import fr.eni.td2j.bookhub_api.feature.user.dto.request.UpdateUserDTO;
import fr.eni.td2j.bookhub_api.feature.user.dto.response.UserResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateProfile(
            Authentication auth,
           @Valid @RequestBody UpdateUserDTO dto
    ) {
        System.out.println("je passe dans le controller de UPDATE");
        UserResponseDTO response = userService.updateProfile(auth.getName(), dto);
        return ResponseEntity.ok(ApiResponse.success(response, "Profil mis à jour avec succès"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteProfile(
            Authentication auth
    ) {
        userService.deleteAccount(auth.getName());
        return ResponseEntity.ok(ApiResponse.success(null, "Profil supprimé avec succès"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/access")
    public ResponseEntity<ApiResponse<Void>> testNoGivenAccess(
            Authentication auth
    ) {

        return ResponseEntity.ok(ApiResponse.success(null, "Test d'accès réussi pour l'utilisateur : " + auth.getName()));
    }

    @GetMapping("/profile")
    public UserResponseDTO me(Authentication auth) {

        return userService.getCurrentUser(auth.getName());
    }
}
