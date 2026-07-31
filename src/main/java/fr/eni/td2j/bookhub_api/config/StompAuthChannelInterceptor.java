package fr.eni.td2j.bookhub_api.config;

import fr.eni.td2j.bookhub_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService; // ton service de validation JWT

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        assert accessor != null;
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            log.info("Headers reçus au CONNECT : {}", accessor.toNativeHeaderMap());
            String token = accessor.getFirstNativeHeader("Authorization");
            log.info("Token extrait : {}", token);
            if (token != null && token.startsWith("Bearer ")) {
                String jwt = token.substring(7);
                try {

                    Authentication auth = jwtService.getAuthentication(jwt); // valide + construit l'Authentication
                    accessor.setUser(auth); // ← essentiel : c'est ça qui identifie le "Principal" du user
                } catch (Exception e) {
                    // log explicite pour voir la vraie cause
                    log.error("Échec authentification WebSocket : {}", e.getMessage(), e);
                    throw new AuthenticationCredentialsNotFoundException("Token invalide", e);

                }
            } else {
                throw new AuthenticationCredentialsNotFoundException("token manquant");
            }
        }
        return message;
    }
}
