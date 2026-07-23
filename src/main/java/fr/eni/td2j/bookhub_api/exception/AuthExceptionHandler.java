package fr.eni.td2j.bookhub_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Intercepte les exceptions liées à l'authentification levées dans les contrôleurs
 * (notamment AuthController) pour les transformer en réponses HTTP propres.
 * <p>
 * Sans cette classe, une BadCredentialsException levée par exemple lors du login
 * remonterait sous forme d'erreur 500 (Internal Server Error), ce qui n'est pas
 * une réponse cohérente pour le client (le problème vient de sa requête, pas du serveur).
 * <p>
 * @RestControllerAdvice fonctionne comme un @ControllerAdvice + @ResponseBody :
 * il s'applique à TOUS les contrôleurs de l'application, et retourne directement
 * le corps de la réponse (ici du texte, potentiellement un JSON structuré plus tard).
 */
@RestControllerAdvice
public class AuthExceptionHandler {

    /**
     * Intercepte une BadCredentialsException (levée par l'AuthenticationManager
     * quand l'email ou le mot de passe est incorrect) et la transforme en réponse 401.
     *
     * @param ex l'exception levée lors de la tentative d'authentification
     * @return une réponse HTTP 401 Unauthorized avec un message générique
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Email ou mot de passe incorrect");
    }
}